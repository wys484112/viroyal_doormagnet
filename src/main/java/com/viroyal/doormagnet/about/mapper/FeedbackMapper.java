package com.viroyal.doormagnet.about.mapper;

import com.viroyal.doormagnet.about.entity.FeedbackEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户反馈持久化操作
 *
 * @Author: wangshun
 * @Date: Create in 2018/1/24 13:21
 */
@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO t_app_feedback (text,phone,user_id) VALUES (#{text},#{phone},#{userId})")
    int insertFeedback(FeedbackEntity feedbackEntity);

}
