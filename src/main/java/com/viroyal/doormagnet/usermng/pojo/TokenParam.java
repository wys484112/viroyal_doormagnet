package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenParam {

    public String token;

    @JsonProperty("user_id")
    public Long userId;

    public TokenParam(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
