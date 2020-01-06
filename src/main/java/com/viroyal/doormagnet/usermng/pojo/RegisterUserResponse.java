package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 注册用户的响应消息
 *
 * @author LiGang
 */
@SuppressWarnings(value = "unused")
@JsonInclude(value = Include.NON_NULL)
public class RegisterUserResponse extends BaseResponse {
    @JsonProperty("user_id")
    public String userId;

    public String token; // 用户在第三方系统中的密码

    public RegisterUserResponse() {

    }

    public RegisterUserResponse(BaseResponse value) {
        super(value);
    }
}
