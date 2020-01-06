package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class ProfileParam {
    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("user_name")
    public String username;

    @JsonProperty("real_name")
    public String realname;

    @JsonProperty("profession")
    public String profession;

    @JsonProperty("overview")
    public String overview;
}
