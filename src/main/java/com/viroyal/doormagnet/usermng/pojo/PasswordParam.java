package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class PasswordParam {
    @JsonProperty("old_password")
    public String oldPassword;

    @JsonProperty("new_password")
    public String newPassword;
}
