package com.viroyal.doormagnet.about.service.Impl;

import java.util.List;

import com.viroyal.doormagnet.about.entity.FeedbackEntity;
import com.viroyal.doormagnet.about.mapper.FeedbackMapper;
import com.viroyal.doormagnet.util.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.about.entity.AdvEntity;
import com.viroyal.doormagnet.about.entity.VersionEntity;
import com.viroyal.doormagnet.about.mapper.AdvMapper;
import com.viroyal.doormagnet.about.mapper.VersionMapper;
import com.viroyal.doormagnet.about.pojo.AdvDataListResponse;
import com.viroyal.doormagnet.about.pojo.BaseResponse;
import com.viroyal.doormagnet.about.pojo.ExtraParamResponse;
import com.viroyal.doormagnet.about.service.AboutService;
import com.viroyal.doormagnet.devicemng.service.impl.DeviceMngImpl;

/**
 * @author walker
 */
@Service
public class AboutServiceImpl implements AboutService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceMngImpl.class);

    @Autowired
    VersionMapper versionMapper;

    @Autowired
    AdvMapper advMapper;

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 获取最新版本号
     *
     * @return BaseResponse
     */
    @Override
    public BaseResponse getLatestVersion() {
        VersionEntity rsp = versionMapper.getLatestVersion();
        return new ExtraParamResponse(rsp);
    }

    /**
     * 获取广告的数据
     *
     * @return BaseResponse
     */
    @Override
    public BaseResponse getAdvData() {
        List<AdvEntity> advDataList = advMapper.getAdvData();
        return new AdvDataListResponse(advDataList);
    }

    /**
     * 插入反馈信息
     *
     * @param feedback 反馈实体
     * @param userId   用户id
     * @return BaseResponse
     */
    @Override
    public BaseResponse feedback(FeedbackEntity feedback, Long userId) {
        if (StringUtils.isBlank(feedback.phone) || StringUtils.isBlank(feedback.text)) {
            return new BaseResponse(ErrorCode.SYSTEM_BUSY, "参数错误");
        }
        feedback.userId = userId;
        feedbackMapper.insertFeedback(feedback);
        return new BaseResponse(ErrorCode.SUCCESS, "插入成功");
    }

}
