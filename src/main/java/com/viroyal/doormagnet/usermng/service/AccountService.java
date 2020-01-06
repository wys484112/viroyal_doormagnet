package com.viroyal.doormagnet.usermng.service;


import com.viroyal.doormagnet.usermng.pojo.*;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public interface AccountService {
    BaseResponse getVerificationMessage(MessageBean param);

    BaseResponse register(RegisterParam param);

    BaseResponse login(LoginParam param);

    BaseResponse forget(ForgetParam param);

    BaseResponse link(LinkParam param, Long userId);

    BaseResponse link(Long id, Long userId);

    BaseResponse link(Long userId);

    BaseResponse password(PasswordParam param, String token);

    BaseResponse profile(ProfileParam param, String token);

    BaseResponse profile(String token);

    BaseResponse phone(PhoneParam param, String token, Long userId);

    BaseResponse logout(String token, Long userId);

    BaseResponse authorization(String token);

    BaseResponse token(BaseParam param);

    BaseResponse h5token(BaseParam param);

    BaseResponse detail(BaseParam param);

    BaseResponse pushId(Long userId, String token, PushParam param);
}
