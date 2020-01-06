package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class ForgetParam {
    @JsonProperty("phone")
    public String phone;

    @JsonProperty("password")
    public String password;

    @JsonProperty("sms_code")
    public String smsCode;

    @JsonProperty("sms_key")
    public String smsKey;
}
