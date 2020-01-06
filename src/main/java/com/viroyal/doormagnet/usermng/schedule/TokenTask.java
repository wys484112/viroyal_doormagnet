package com.viroyal.doormagnet.usermng.schedule;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.qiniu.util.Auth;
import com.viroyal.doormagnet.usermng.entity.SysConfigEntity;
import com.viroyal.doormagnet.usermng.exception.NoOriginDataException;
import com.viroyal.doormagnet.usermng.pojo.StoreEntity;
import com.viroyal.doormagnet.usermng.repository.SysConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@Component
@EnableScheduling
public class TokenTask {
    private static final Logger logger = LoggerFactory.getLogger(TokenTask.class);

    private static final String KEY = "QINIU";

    private static final String ACCESSKEY = "AJVHmJQfA1HvOP2kJxq0UqfA_QCPBKt6Pp_COdNx";

    private static final String SECRETKEY = "zIb4ZRLFV2sA8cRuh8QuY1e7OiqwNVQgyBQELXng";

    private static final Auth auth = Auth.create(ACCESSKEY, SECRETKEY);

    @Autowired
    private SysConfigRepository sysConfigRepository;

    //@Scheduled(fixedDelay = 1000 * 3600 * 24 * 2) //两天执行一次
    @Transactional
    public void scheduleUpdateToken() {
        try {
            SysConfigEntity entity = sysConfigRepository.findByCodeAndDeletedAtIsNull(KEY);
            if (entity == null) throw new NoOriginDataException(KEY);

            String value = entity.getValue();
            if (value == null) throw new NoOriginDataException(KEY);

            logger.info("qiniu value before update: " + value);
            List<StoreEntity> storeList = new Gson().fromJson(entity.getValue(), new TypeToken<List<StoreEntity>>() {
            }.getType());
            for (StoreEntity store : storeList) {
                store.uploadToken = auth.uploadToken(store.bucketName, null, 60 * 60 * 24 * 365 * 20, null, true);
            }

            value = new Gson().toJson(storeList);
            logger.info("qiniu value after update: " + value);
            entity.setValue(value);
            entity.setUpdatedAt(new Date());
            sysConfigRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            logger.error("违反该数据库表数据完整性: " + e.getCause().getMessage());
        } catch (NoOriginDataException e) {
            logger.error("数据库中原始数据异常: " + e.getMessage());
        } catch (Exception e) {
            logger.error("七牛其它异常: " + e.getCause().toString());
        }
    }
}
