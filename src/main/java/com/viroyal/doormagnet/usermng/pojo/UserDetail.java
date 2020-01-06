package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Created by qinyl.
 * @date: 2017/3/4.
 * @comments:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetail {
    @JsonProperty("username")
    public String username;

    @JsonProperty("realname")
    public String realname;

    @JsonProperty("phone")
    public String phone;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty("overview")
    private String overview;

    public UserDetail(String username, String realname, String phone, String avatarUrl, String profession, String overview) {
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.profession = profession;
        this.overview = overview;
    }
}
