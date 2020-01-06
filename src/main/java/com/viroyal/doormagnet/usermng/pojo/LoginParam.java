package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class LoginParam {
    @JsonProperty("acc_name")
    public String accName;

    @JsonProperty("password")
    public String password;

    @JsonProperty("type")
    public Integer type;

    @JsonProperty("token")
    public String token;

    @JsonProperty("info")
    public String info;
}
