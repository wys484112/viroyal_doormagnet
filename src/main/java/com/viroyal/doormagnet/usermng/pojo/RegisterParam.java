package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class RegisterParam {
    @JsonProperty("type")
    public Integer type;

    @JsonProperty("acc_name")
    public String accName;

    @JsonProperty("password")
    public String password;

    @JsonProperty("sms_code")
    public String smsCode;

    @JsonProperty("sms_key")
    public String smsKey;

    @JsonProperty("user_name")
    public String username;

    @JsonProperty("avatar_url")
    public String avatarUrl;
}
