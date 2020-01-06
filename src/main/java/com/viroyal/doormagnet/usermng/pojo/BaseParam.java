package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Created by qinyl.
 * @date: 2017/3/2.
 * @comments:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseParam {
    @JsonProperty("user_id")
    public Long userId;

    @JsonProperty("token")
    public String token;

    public BaseParam() {

    }

    public BaseParam(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
