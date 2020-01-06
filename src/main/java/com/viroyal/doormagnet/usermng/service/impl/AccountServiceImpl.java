package com.viroyal.doormagnet.usermng.service.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.viroyal.doormagnet.usermng.entity.AccountEntity;
import com.viroyal.doormagnet.usermng.entity.SysConfigEntity;
import com.viroyal.doormagnet.usermng.entity.UserEntity;
import com.viroyal.doormagnet.usermng.exception.*;
import com.viroyal.doormagnet.usermng.http.HttpClient;
import com.viroyal.doormagnet.usermng.pojo.*;
import com.viroyal.doormagnet.usermng.repository.AccountRepository;
import com.viroyal.doormagnet.usermng.repository.SysConfigRepository;
import com.viroyal.doormagnet.usermng.repository.UserRepository;
import com.viroyal.doormagnet.usermng.rong.ApiHttpClient;
import com.viroyal.doormagnet.usermng.rong.models.FormatType;
import com.viroyal.doormagnet.usermng.rong.models.SdkHttpResult;
import com.viroyal.doormagnet.usermng.service.AccountService;
import com.viroyal.doormagnet.usermng.util.JsonUtil;
import com.viroyal.doormagnet.usermng.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.*;
import java.util.stream.Collectors;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String HTTP = "http://";

    private static final String HTTPS = "https://";

    private static final String GET_MESSAGE = "/sms/getSms";

    private final static String CHECK_MESSAGE = "/sms/checkSmsInfo";

    private static final String CODE = "QINIU";

    private static final String mAppKey = "c9kqb3rdcec2j";//"8luwapkvuzd0l";

    private static final String mSecretKey = "EW1T7Un2MI";//"DqmhCBCkD6lc";

    private static final int RETRY_COUNT = 3;

    @Value("${smshost}")
    private String SMS_HOST;

    @Value("${smsport}")
    private String SMS_PORT;

    @Value("${sms_appid}")
    private String smsAppId;

    @Value("${sms_app_key}")
    private String smsAppKey;

    @Value("${sms_app_secret}")
    private String smsAppSecret;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private Environment mEnvironment;

    private boolean mIsProductEnv;

    @Autowired
    private JPushClient mJPushClient;

    @Override
    public BaseResponse getVerificationMessage(MessageBean param) {
        try {
            logger.info("Get Verification Message, phone: {}, action: {} ", param.phone, param.action);
            UserEntity entity = userRepository.findByPhoneAndDeletedAtIsNull(param.phone);
            if ("reg".equals(param.action) && entity != null) {
                throw new AccountAlreadyExistException(param.phone + " has already exist");
            }

            if ("forget".equals(param.action) && entity == null) {
                throw new AccountNonExistException(param.phone + " no exist");
            }

            if (("bindphone".equals(param.action) || "webreg".equals(param.action)) && entity != null) {
                throw new PhoneUsedException(param.phone + " been used");
            }

            MessageResponse response = HttpClient.Builder.messageHttpClient().sendMessage(smsAppId, smsAppKey, smsAppSecret, new MessageSendParam(param.phone, "common", 0));
            response.setErrorCode(response.error_code);
            response.setErrorMsg(response.error_msg);
            return response;
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "账户信息异常");
        } catch (AccountAlreadyExistException e) {
            logger.info(e.getMessage());
            return new BaseResponse(1056, "账号已存在，请直接登录");
        } catch (AccountNonExistException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1057, "账户不存在");
        } catch (PhoneUsedException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1061, "该手机号已被其他账户使用");
        } catch (ResourceAccessException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(1051, "系统忙，请稍后再试");
        } catch (MessageGetFailException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1052, "系统忙，请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse register(RegisterParam param) {
        return param.type == 0 ? register(param.type, param.accName, param.password, param.smsCode, param.smsKey)
                : register(param.type, param.accName, param.username, param.avatarUrl);
    }

    public BaseResponse register(Integer type, String phone, String password, String code, String key) {
        try {
            if (!checkMessage(phone, code, key, "reg").isSuccess()) {
                throw new MessageCheckFailException("phone: " + phone + ", code_" + code + ", key_" + code);
            }

            UserEntity entity = userRepository.findByPhoneAndDeletedAtIsNull(phone);
            String token = TokenUtil.token(System.currentTimeMillis() + phone);
            String h5token = TokenUtil.token(token + phone);
            Date date = new Date();

            if (entity == null) {
                entity = new UserEntity();
                entity.setPhone(phone);
                entity.setToken(token);
                entity.setUsername(phone);
                entity.setH5token(h5token);
                entity.setCreatedAt(date);
                entity.setUpdatedAt(date);
                entity.getAccountEntities().add(new AccountEntity(phone, password, type, date, date, entity));
                entity = userRepository.save(entity);

                entity.userId = String.valueOf(entity.getId());

            } else {
                Set<AccountEntity> accountEntities = entity.getAccountEntities();
                String phone2 = entity.getPhone();
                Optional<AccountEntity> optional =
                        accountEntities.stream().filter(t -> phone2.equals(t.getAccName())).findFirst();
                if (!optional.isPresent()) {
                    throw new AccountNonExistException("phone: " + entity.getPhone());
                }

                optional.get().setLoginAt(date);
                optional.get().setUpdatedAt(date);
                entity.setToken(token);
                //entity.setH5token(h5token);
                entity.setUpdatedAt(date);
                entity = userRepository.save(entity);
                entity.userId = String.valueOf(entity.getId());
                entity.setUsername(phone2);
            }

           /* if (StringUtils.isBlank(entity.getImId()) || StringUtils.isBlank(entity.getImToken())) {
                RegisterUserResponse response = registerPushUser(entity.getId());
                if (!response.isSuccess()) {
                    logger.error("push user: register failure");
                } else {
                    logger.info("im token info: " + response.userId + "_" + response.token);
                    entity.setImId(response.userId);
                    entity.setImToken(response.token);
                    userRepository.save(entity);
                }
            }*/

            return new AccountResponse(1000, "", new Extra(entity, tokenStore()));
        } catch (AccountJsonException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "序列化短消息参数异常");
        } catch (RemoteMessageServerException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(1051, "系统忙，请稍后再试");
        } catch (MessageCheckFailException e) {
            logger.info("message check failure: " + e.getMessage());
            return new BaseResponse(1053, "验证码不正确");
        } catch (AccountAlreadyExistException e) {
            logger.info(e.getMessage());
            return new BaseResponse(1056, "账号已存在，请直接登录");
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "保存到数据库的数据非法");
        } catch (AccountNonExistException e) {
            logger.error("phone: " + e.getMessage() + ", non exist");
            return new BaseResponse(1057, "该账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    public BaseResponse register(Integer type, String accName, String username, String avatarUrl) {
        try {
            AccountEntity entity = accountRepository.findByTypeAndAccNameAndDeletedAtIsNull(type, accName);
            String token = TokenUtil.token(System.currentTimeMillis() + accName);
            String h5token = TokenUtil.token(token + accName);
            Date date = new Date();

            if (entity == null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(username);
                userEntity.setToken(token);
                userEntity.setH5token(h5token);
                userEntity.setAvatarUrl(avatarUrl);
                userEntity.setCreatedAt(date);
                userEntity.setUpdatedAt(date);
                userEntity.getAccountEntities().add(new AccountEntity(accName, "", type, date, date, userEntity));

                userEntity = userRepository.save(userEntity);
               /* if (StringUtils.isBlank(userEntity.getImId()) || StringUtils.isBlank(userEntity.getImToken())) {
                    RegisterUserResponse response = registerPushUser(userEntity.getId());
                    if (!response.isSuccess()) {
                        logger.error("push user: register failure");
                    } else {
                        logger.info("im token info: " + response.userId + "_" + response.token);
                        userEntity.setImId(response.userId);
                        userEntity.setImToken(response.token);
                        userRepository.save(userEntity);
                    }
                }*/

                userEntity.userId = String.valueOf(userEntity.getId());
                return new AccountResponse(1000, "", new Extra(userEntity, tokenStore()));
            } else {
                logger.info("This third account already exist: return directly");
                UserEntity userEntity = entity.getUserEntity();
               /* if (userEntity.getImId() == null || userEntity.getImToken() == null) {
                    RegisterUserResponse response = registerPushUser(userEntity.getId());
                    if (!response.isSuccess()) {
                        logger.error("push user: register failure");
                    } else {
                        logger.info("im token info: " + response.userId + "_" + response.token);
                        userEntity.setImId(response.userId);
                        userEntity.setImToken(response.token);
                    }
                }*/
                entity.setLoginAt(date);
                entity.setUpdatedAt(date);
                userEntity.setToken(token);
                userEntity.setH5token(h5token);
                if (userEntity.getAvatarUrl() == null) {
                    userEntity.setAvatarUrl(avatarUrl);
                }
                userEntity.setUpdatedAt(date);
                entity = accountRepository.save(entity);
                entity.getUserEntity().userId = String.valueOf(entity.getUserEntity().getId());
                return new AccountResponse(1000, "", new Extra(entity.getUserEntity(), tokenStore()));
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "数据库中存在多条相同记录异常: " + type + ", name: " + accName);
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse login(LoginParam param) {
        return param.type == 0 ?
                login(param.accName, param.password, param.info) :
                login(param.token, param.info);
    }

    public BaseResponse login(String phone, String password, String info) {
        try {
            AccountEntity entity = accountRepository.findByAccNameAndDeletedAtIsNull(phone);
            if (entity == null) {
                throw new AccountNonExistException(phone);
            }

            entity = accountRepository.findByAccNameAndPasswordAndDeletedAtIsNull(phone, password);
            if (entity == null) {
                throw new AccountWrongPasswordException(phone);
            }

            String token = TokenUtil.token(System.currentTimeMillis() + phone);
            String h5token = TokenUtil.token(token + phone);
            Date date = new Date();
            if (StringUtils.isBlank(entity.getInfo())) {
                entity.setInfo(info);
            }

            entity.setLoginAt(date);
            entity.setUpdatedAt(date);
            UserEntity userEntity = entity.getUserEntity();
            /*if (StringUtils.isBlank(userEntity.getImId()) || StringUtils.isBlank(userEntity.getImToken())) {
                RegisterUserResponse response = registerPushUser(userEntity.getId());
                if (!response.isSuccess()) {
                    logger.error("push user: register failure");
                } else {
                    logger.info("im token info: " + response.userId + "_" + response.token);
                    userEntity.setImId(response.userId);
                    userEntity.setImToken(response.token);
                }
            }*/
            userEntity.setToken(token);
            userEntity.setH5token(h5token);
            userEntity.setUpdatedAt(date);
            entity = accountRepository.save(entity);

            entity.getUserEntity().userId = String.valueOf(entity.getUserEntity().getId());
            return new AccountResponse(1000, "", new Extra(entity.getUserEntity(), tokenStore()));
        } catch (AccountNonExistException e) {
            logger.info("Account non exist: " + e.getMessage());
            return new BaseResponse(1057, "该账户不存在");
        } catch (AccountWrongPasswordException e) {
            logger.info("Wrong password: " + e.getMessage());
            return new BaseResponse(1058, "密码不正确");
        } catch (Exception e) {
            logger.error("", e);
            return new BaseResponse(7005, "其它异常");
        }
    }

    public BaseResponse login(String token, String info) {
        try {
            UserEntity entity = userRepository.findByTokenAndDeletedAtIsNull(token);
            if (entity == null) {
                throw new AccountNonExistException(token);
            }

            /*if (StringUtils.isBlank(entity.getImId()) || StringUtils.isBlank(entity.getImToken())) {
                RegisterUserResponse response = registerPushUser(entity.getId());
                if (!response.isSuccess()) {
                    logger.error("push user: register failure");
                } else {
                    logger.info("im token info: " + response.userId + "_" + response.token);
                    entity.setImId(response.userId);
                    entity.setImToken(response.token);
                    userRepository.save(entity);
                }
            }*/

            entity.userId = String.valueOf(entity.getId());
            return new AccountResponse(1000, "", new Extra(entity, tokenStore()));
        } catch (AccountNonExistException e) {
            logger.info("no account of this token: " + e.getMessage());
            return new BaseResponse(1055, "登录信息已失效，请重新登录");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse forget(ForgetParam param) {
        try {
            if (!checkMessage(param.phone, param.smsCode, param.smsKey, "common").isSuccess()) {
                throw new MessageCheckFailException("phone: " + param.phone + ", code" + param.smsCode + ", key" + param.smsKey);
            }

            AccountEntity entity = accountRepository.findByAccNameAndDeletedAtIsNull(param.phone);
            if (entity == null) {
                throw new AccountNonExistException(param.phone);
            }

            entity.setPassword(param.password);
            entity.setUpdatedAt(new Date());
            accountRepository.save(entity);
            return new BaseResponse(1000, "");
        } catch (MessageCheckFailException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1053, "验证码不正确");
        } catch (AccountJsonException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "序列化短消息参数异常");
        } catch (RemoteMessageServerException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(1051, "系统忙，请稍后再试");
        } catch (AccountNonExistException e) {
            logger.error(e.getMessage() + " non exist");
            return new BaseResponse(1057, "账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse link(LinkParam param, Long userId) {
        try {
            AccountEntity entity = accountRepository.findByAccNameAndTypeAndDeletedAtIsNull(param.accName, param.type);
            if (entity != null) {
                if (entity.getUserId().equals(userId))
                    throw new AccountAlreadyLinkedByItSelfException(userId + "_" + param.accName + "_" + param.type);
                else {
                    //throw new AccountAlreadyLinkedException(param.accName + "_" + param.type);
                    //合并账号
                    entity.setUserId(userId);
                    accountRepository.save(entity);
                    return new BaseResponse(1000, "");
                }
            }

            entity = accountRepository.findByuserEntity_IdAndTypeAndDeletedAtIsNull(userId, param.type);
            Date date = new Date();
            if (entity == null) {
                entity = new AccountEntity();
                entity.setUserId(userId);
                entity.setAccName(param.accName);
                entity.setType(param.type);
                entity.setCreatedAt(date);
                entity.setUpdatedAt(date);
                entity.setLoginAt(date);
                accountRepository.save(entity);
            } else {
                entity.setAccName(param.accName);
                entity.setUpdatedAt(date);
                entity.setLoginAt(date);
                accountRepository.save(entity);
            }

            return new BaseResponse(1000, "");
        } catch (AccountAlreadyLinkedException e) {
            logger.info("This third account already linked: " + e.getMessage());
            return new BaseResponse(1063, "该QQ号/微信已被其它账户绑定");
        } catch (AccountAlreadyLinkedByItSelfException e) {
            logger.info("This third account already linked by itself: " + e.getMessage());
            return new BaseResponse(1000, "");
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "数据库中存在多条相同记录异常: " + param.type + ", name: " + param.accName);
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse link(Long id, Long userId) {
        try {
            AccountEntity entity = accountRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId);
            if (entity == null) {
                throw new AccountNonExistException(userId.toString());
            }

            accountRepository.delete(entity);
            return new BaseResponse(1000, "");
        } catch (AccountNonExistException e) {
            logger.error("unlink: account non exist: " + e.getMessage());
            return new BaseResponse(1057, "账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse link(Long userId) {
        try {
            List<AccountEntity> entity = accountRepository.findByUserIdAndDeletedAtIsNull(userId);
            if (entity.isEmpty()) {
                throw new AccountNonExistException("userId: " + userId);
            }

            return new AccountResponse(1000, "",
                    entity.stream().map(t -> new Extra(t.getId(),
                            t.getType(),
                            t.getAccName(),
                            t.getType() != 0 ? null : ((StringUtils.isBlank(t.getPassword()) || t.getPassword().length() < 32) ? 0 : 1))
                    ).collect(Collectors.toList()));
        } catch (AccountNonExistException e) {
            logger.error("Account non exist: " + e.getMessage());
            return new BaseResponse(1057, "账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse password(PasswordParam param, String token) {
        try {
            if (param.newPassword.equals(param.oldPassword)) {
                throw new AccountOldNewPwdSameException(token);
            }

            UserEntity entity = userRepository.findByTokenAndDeletedAtIsNull(token);
            if (entity == null) {
                throw new AccountNonExistException("token: " + token);
            }

            Set<AccountEntity> accountEntities = entity.getAccountEntities();
            AccountEntity accountEntity;
            String phone = entity.getPhone();
            if (phone != null) {
                Optional<AccountEntity> optional = accountEntities.stream().filter(t -> phone.equals(t.getAccName())).findFirst();
                if (!optional.isPresent()) {
                    throw new AccountNonExistException("phone: " + entity.getPhone());
                }

                accountEntity = optional.get();
            } else {
                accountEntity = accountEntities.stream().findFirst().get();
            }

            if (StringUtils.isBlank(param.oldPassword)) {
                accountEntity.setPassword(param.newPassword);
                accountEntity.setUpdatedAt(new Date());
                entity = userRepository.save(entity);

            } else {
                if (!param.oldPassword.equals(accountEntity.getPassword())) {
                    if (StringUtils.isNotBlank(accountEntity.getPassword()))
                        throw new AccountOldPwdWrongException(token);
                }
                accountEntity.setPassword(param.newPassword);
                accountEntity.setUpdatedAt(new Date());
                entity = userRepository.save(entity);
            }

            entity.userId = String.valueOf(entity.getId());
            return new AccountResponse(1000, "", new Extra(entity, tokenStore()));
        } catch (AccountOldNewPwdSameException e) {
            logger.error(e.getMessage() + " old equal new one");
            return new BaseResponse(1059, "新旧密码不能相等");
        } catch (AccountNonExistException e) {
            logger.error(e.getMessage() + " non exist");
            return new BaseResponse(1057, "账户不存在");
        } catch (AccountOldPwdWrongException e) {
            logger.error(e.getMessage() + " wrong old password");
            return new BaseResponse(1060, "旧密码不正确");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse profile(ProfileParam param, String token) {
        try {
            UserEntity entity = userRepository.findByTokenAndDeletedAtIsNull(token);
            if (entity == null) {
                throw new AccountNonExistException(token);
            }

            if (StringUtils.isNotBlank(param.avatarUrl))
                entity.setAvatarUrl(param.avatarUrl);
            if (StringUtils.isNotBlank(param.username))
                entity.setUsername(param.username);
            if (StringUtils.isNotBlank(param.realname))
                entity.setRealname(param.realname);
            if (StringUtils.isNotBlank(param.profession))
                entity.setProfession(param.profession);
            if (StringUtils.isNotBlank(param.overview))
                entity.setOverview(param.overview);

            userRepository.save(entity);

           /* BaseResponse response = cmsFeign.userUpdate((String) MDC.get(RandomUtil.MDC_KEY), new TokenParam(null, entity.getId()));
            logger.info("userUpdate response: " + response.getErrorCode());*/
            return new BaseResponse(1000, "");
        } catch (AccountNonExistException e) {
            logger.error(e.getMessage() + " non exist");
            return new BaseResponse(1057, "账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse profile(String token) {
        try {
            UserEntity entity = userRepository.findByTokenAndDeletedAtIsNull(token);
            if (entity == null) {
                throw new AccountNonExistException(token);
            }

            return new AccountResponse(1000, "",
                    new Extra(entity.getAvatarUrl(),
                            entity.getRealname(),
                            entity.getUsername(),
                            entity.getProfession(),
                            entity.getOverview()
                    )
            );
        } catch (AccountNonExistException e) {
            logger.error(e.getMessage() + " non exist");
            return new BaseResponse(1057, "账户不存在");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse phone(PhoneParam param, String token, Long userId) {
        try {
            if (!checkMessage(param.phone, param.smsCode, param.smsKey, "bindphone").isSuccess()) {
                throw new MessageCheckFailException("phone: " + param.phone + ", code" + param.smsCode + ", key" + param.smsKey);
            }

            UserEntity entity = userRepository.findByPhoneAndDeletedAtIsNull(param.phone);
            if (entity != null) {
                throw new PhoneUsedException(param.phone);
            }

            List<AccountEntity> entities = accountRepository.findByUserIdAndDeletedAtIsNull(userId);
            if (entities.isEmpty()) {
                throw new AccountNonExistException(String.valueOf(userId));
            }

            Optional<AccountEntity> optional = entities.stream().filter(t -> t.getType() == 0).findFirst();
            AccountEntity accountEntity;
            Date date = new Date();
            if (!optional.isPresent()) {
                accountEntity = new AccountEntity();
                accountEntity.setUserId(userId);
                accountEntity.setAccName(param.phone);
                accountEntity.setType(0);
                accountEntity.setCreatedAt(date);
                accountEntity.setUpdatedAt(date);
                accountRepository.save(accountEntity);
                entity = entities.get(0).getUserEntity();
                entity.setPhone(param.phone);
                entity.setUpdatedAt(date);
                userRepository.save(entity);
            } else {
                accountEntity = optional.get();
                accountEntity.setAccName(param.phone);
                accountEntity.setUpdatedAt(date);
                accountEntity.getUserEntity().setPhone(param.phone);
                accountEntity.getUserEntity().setUpdatedAt(date);
                accountRepository.save(accountEntity);
            }

            return new BaseResponse(1000, "");
        } catch (MessageCheckFailException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1053, "验证码不正确");
        } catch (AccountNonExistException e) {
            logger.error("Account non exist: " + e.getMessage());
            return new BaseResponse(1057, "账户不存在");
        } catch (PhoneUsedException e) {
            logger.error("Phone been used: " + e.getMessage());
            return new BaseResponse(1061, "该手机号已被其他账户使用");
        } catch (AccountIntegrityBrokenException e) {
            logger.error("Broken account: " + e.getMessage());
            return new BaseResponse(1062, "系统忙，请稍后再试");
        } catch (Exception e) {
            logger.error(e.getCause().toString());
            return new BaseResponse(7005, "其它异常");
        }
    }

    @Override
    public BaseResponse logout(String token, Long userId) {
        try {
            if (StringUtils.isBlank(token) || userId == null) {
                throw new AccountIllegalArgumentException("illegal parameter: token and user_id must not be null");
            }

            UserEntity entity = userRepository.findByIdAndTokenAndDeletedAtIsNull(userId, token);
            if (entity != null) {
                entity.setToken(null);
                entity.setImToken(null);
                entity.setUpdatedAt(new Date());
                userRepository.save(entity);
            }

            return new BaseResponse(1000, "登出成功");
        } catch (AccountIllegalArgumentException e) {
            logger.error(e.getMessage());
            return new BaseResponse(1001, "参数错误");
        } catch (Exception e) {
            logger.error("logout exception: " + e.getCause().toString());
            return new BaseResponse(7005, "登出异常");
        }
    }

    private BaseResponse checkMessage(final String phone, final String code, final String key, final String action) {
        MessageResponse response = HttpClient.Builder.messageHttpClient()
                .verifyMessage(smsAppId, smsAppKey, smsAppSecret, new MessageVerifyParam(phone, "common", code, key));
        response.setErrorCode(response.error_code);
        response.setErrorMsg(response.error_msg);
        return response;
    }

    @Override
    public BaseResponse authorization(String token) {
        try {
            UserEntity entity = userRepository.findByTokenAndDeletedAtIsNull(token);
            if (entity == null) throw new AccountBadTokenException(token);
            return new BaseResponse(1000, "");
        } catch (AccountBadTokenException e) {
            logger.info("token expire: " + e.getMessage());
            return new BaseResponse(1055, "登录信息已失效，请重新登录");
        } catch (JpaSystemException e) {
            return new BaseResponse(1000, "");
        } catch (Exception e) {
            logger.error("token check exception: " + e.getCause().toString());
            return new BaseResponse(7005, "校验TOKEN异常");
        }
    }

    @Override
    public BaseResponse token(BaseParam param) {
        try {
            logger.info("token check by other module");
            UserEntity entity = null;
            if (param.userId != null && StringUtils.isNotBlank(param.token)) {
                entity = userRepository.findByIdAndTokenAndDeletedAtIsNull(param.userId, param.token);
            } else if (param.token != null) {
                entity = userRepository.findByTokenAndDeletedAtIsNull(param.token);
            }

            if (entity == null) throw new AccountBadTokenException(param.userId + "_" + param.token);
            return new ExtraParamResponse<>(new BaseParam(entity.getId(), null));
        } catch (AccountBadTokenException e) {
            logger.info("token expire: " + e.getMessage());
            return new BaseResponse(1055, "登录信息已失效，请重新登录");
        } catch (JpaSystemException e) {
            return new BaseResponse(1000, "");
        } catch (Exception e) {
            logger.error("token check exception: " + e.getCause().toString());
            return new BaseResponse(7005, "校验TOKEN异常");
        }
    }

    @Override
    public BaseResponse h5token(BaseParam param) {
        try {
            logger.info("h5token check by other module");
            UserEntity entity = userRepository.findByH5tokenAndDeletedAtIsNull(param.token);
            if (entity == null) throw new AccountBadTokenException(param.token);
            return new ExtraParamResponse<>(new BaseParam(entity.getId(), null));
        } catch (AccountBadTokenException e) {
            logger.info("h5token expire: " + e.getMessage());
            return new BaseResponse(1055, "登录信息已失效，请重新登录");
        } catch (JpaSystemException e) {
            return new BaseResponse(1000, "");
        } catch (Exception e) {
            logger.error("h5token check exception: " + e.getCause().toString());
            return new BaseResponse(7005, "校验TOKEN异常");
        }
    }

    @Override
    public BaseResponse detail(BaseParam param) {
        try {
            logger.info("get detail by other module");
            UserEntity entity = userRepository.findByIdAndDeletedAtIsNull(param.userId);
            if (entity == null) throw new AccountNonExistException(String.valueOf(param.userId));
            String username = entity.getUsername();
            logger.info("username: " + username);
            return new AccountResponse(1000, "",
                    new UserDetail(entity.getUsername(),
                            entity.getRealname(),
                            entity.getPhone(),
                            entity.getAvatarUrl(),
                            entity.getProfession(),
                            entity.getOverview()
                    )
            );
        } catch (AccountNonExistException e) {
            logger.info("account non exist: " + e.getMessage());
            return new BaseResponse(1057, "账户不存在");
        } catch (JpaSystemException e) {
            return new BaseResponse(1000, "");
        } catch (Exception e) {
            logger.error("detail exception: " + e.getCause().toString());
            return new BaseResponse(7005, "获取账户异常");
        }
    }

    @Override
    public BaseResponse pushId(Long userId, String token, PushParam param) {
        try {
            if (userId == null || StringUtils.isBlank(token)) {
                throw new AccountIllegalArgumentException("userId: " + userId + ", token: " + token);
            }

            UserEntity entity = userRepository.findByIdAndTokenAndDeletedAtIsNull(userId, token);
            if (entity == null) {
                throw new AccountBadTokenException(userId + "_" + token);
            }
            List<UserEntity> list = userRepository.findByImToken(param.registrationId);
            for (UserEntity userEntity : list) {
                userEntity.setImToken(null);
            }
            userRepository.save(list);
            //增加踢出处理，如果registrationId变了，就认为是在另外手机上登录了
            if (!TextUtils.isEmpty(entity.getImToken()) && !param.registrationId.equals(entity.getImToken()) ) {
                sendKickPush(userId, entity.getImToken());
            }

            entity.setImToken(param.registrationId);
            userRepository.save(entity);
            return BaseResponse.SUCCESS;
        } catch (AccountIllegalArgumentException e) {
            logger.info(e.getMessage());
            return new BaseResponse(1001, "参数错误");
        } catch (AccountBadTokenException e) {
            logger.info("token expire: " + e.getMessage());
            return new BaseResponse(1055, "登录信息已失效，请重新登录");
        } catch (JpaSystemException e) {
            return new BaseResponse(1000, "");
        } catch (Exception e) {
            logger.error("token check exception: " + e.getCause().toString());
            return new BaseResponse(7005, "校验TOKEN异常");
        }
    }

    private List<TokenStore> tokenStore() {
        try {
            SysConfigEntity entity = sysConfigRepository.findByCodeAndDeletedAtIsNull(CODE);
            if (entity == null) {
                throw new QiNiuTokenNonExistException("qiniu token: " + CODE);
            }
            String value = entity.getValue();
            logger.info("qiniu token value from database: " + value);
            return value == null ? null : new Gson().fromJson(value, new TypeToken<List<StoreEntity>>() {
            }.getType());
        } catch (QiNiuTokenNonExistException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("QINIU Exception: " + e.getCause().getCause().toString());
        }

        return null;
    }

    public RegisterUserResponse registerPushUser(Long userId) {
        String userName = "idedu_user" + userId;
        logger.info("push userId: " + userName);

        int retries = 0;
        BaseResponse rsp;
        SdkHttpResult result;

        RongResponse serverRsp = new RongResponse();
        boolean doRetry;
        do {
            retries++;
            doRetry = false;

            try {
                result = ApiHttpClient.getToken(mAppKey, mSecretKey, userName, userName, null, FormatType.json);
                rsp = checkResponse(result, retries, serverRsp);
                if (rsp.isSuccess()) {
                    if (!userName.equals(serverRsp.userId) || TextUtils.isEmpty(serverRsp.token)) {
                        logger.error("rsp from rong not expected:" + serverRsp);
                        rsp = new BaseResponse(1002, "exception from rong");
                    }
                }
            } catch (ConnectException e) {
                logger.error("connect Rong server failed:" + e.getMessage());
                rsp = new BaseResponse(1002, "网络错误");
                doRetry = true;
            } catch (Exception e) {
                logger.error("exception from rong:" + e);
                rsp = new BaseResponse(1002, "exception from rong");
            }

        } while (doRetry && retries < RETRY_COUNT);

        RegisterUserResponse retRsp = new RegisterUserResponse(new BaseResponse(rsp.getErrorCode(), rsp.getErrorMsg()));
        retRsp.userId = serverRsp.userId;
        retRsp.token = serverRsp.token;
        return retRsp;
    }

    /**
     * 融云的返回都是用code 200表示成功的
     *
     * @param result
     * @return
     */
    private BaseResponse checkResponse(SdkHttpResult result, int retryNum, RongResponse rongRsp) {
        String tag = "retry:" + retryNum;

        if (result.getHttpCode() != HttpStatus.SC_OK) {
            logger.error(tag + " rsp from rong is wrong:" + result);
            return new BaseResponse(1002, "exception from rong");
        }

        if (TextUtils.isEmpty(result.getResult())) {
            logger.error(tag + " rsp from rong is empty:" + result);
            return new BaseResponse(1002, "rsp from rong is empty");
        }

        String strBody = result.getResult();
        RongResponse rsp = JsonUtil.readObject(strBody, RongResponse.class);
        if (rsp == null) {
            logger.error(tag + " invalid rsp from rong:" + strBody);
            return new BaseResponse(1002, "rsp from rong is invalid");
        }

        if (rsp.code != 200) {
            logger.error(tag + " not success from rong:" + strBody);
            return new BaseResponse(1002, "rong rsp code not 200");
        }
        if (rongRsp != null) {
            rongRsp.code = rsp.code;
            rongRsp.userId = rsp.userId;
            rongRsp.token = rsp.token;
        }

        return BaseResponse.SUCCESS;
    }

    private void sendKickPush(Long userId, String registrationId) {
        logger.info("send push to user: {}  push_id={}", userId, registrationId);

        final PushPayload payload = buildPushObject(registrationId);
        sendJPush(payload);
    }

    private PushPayload buildPushObject(String registrationId) {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("action", "kicked");

        PushPayload.Builder builder = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationId))
                .setNotification(Notification.newBuilder()
                        .setAlert("")
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setContentAvailable(true)
                                .setBadge(0)
                                .addExtras(extras)
                                .build())
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent("kicked")
                        .setTitle("test")
                        .build());

        if (mIsProductEnv) {
            builder = builder.setOptions(Options.newBuilder()
                    .setApnsProduction(true).build());
        }
        return builder.build();
    }

    private void sendJPush(final PushPayload payload) {
        try {
            PushResult result = mJPushClient.sendPush(payload);
            logger.info("Got result - " + result);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            logger.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e.getErrorMessage());
        }
    }

    @PostConstruct
    private void postInit() {
        mIsProductEnv = false;
        String[] profiles = mEnvironment.getActiveProfiles();
        if (profiles != null && profiles.length > 0 && "prod".equals(profiles[0])) {
            mIsProductEnv = true;
        }
    }
}
