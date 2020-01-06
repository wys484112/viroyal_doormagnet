package com.viroyal.doormagnet.about.service;

import com.viroyal.doormagnet.about.entity.FeedbackEntity;
import com.viroyal.doormagnet.about.pojo.BaseResponse;

/**
 * 其他业务
 *
 * @author walker
 */
public interface AboutService {
    /**
     * 获取最新版本
     *
     * @return BaseResponse
     */
    BaseResponse getLatestVersion();

    /**
     * 获取广告数据
     *
     * @return BaseResponse
     */
    BaseResponse getAdvData();

    /**
     * 收集用户反馈
     *
     * @param feedback 反馈实体
     * @return BaseResponse
     */
    BaseResponse feedback(FeedbackEntity feedback, Long userId);

}
