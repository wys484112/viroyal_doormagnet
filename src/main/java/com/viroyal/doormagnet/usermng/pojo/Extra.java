package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viroyal.doormagnet.usermng.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Extra implements Serializable {
    @JsonProperty("avatar_url")
    public String avatarUrl;
    @JsonProperty("real_name")
    public String realName;
    @JsonProperty("user_name")
    public String userName;
    @JsonProperty("profession")
    public String profession;
    @JsonProperty("overview")
    public String overview;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("acc_name")
    private String accName;
    @JsonProperty("had_password")
    private Integer hadPassword;
    @JsonProperty("user")
    private UserEntity userEntity;

    @JsonProperty("store")
    private List<TokenStore> tokenStoreList;

    public Extra() {
    }

    public Extra(String avatarUrl, String realName, String userName, String profession, String overview) {
        this.avatarUrl = avatarUrl;
        this.realName = realName;
        this.userName = userName;
        this.profession = profession;
        this.overview = overview;
    }

    public Extra(UserEntity userEntity, List<TokenStore> tokenStoreList) {
        this.userEntity = userEntity;
        this.tokenStoreList = tokenStoreList;
    }

    public Extra(Long id, Integer type, String accName, Integer hadPassword) {
        this.id = id;
        this.type = type;
        this.accName = accName;
        this.hadPassword = hadPassword;
    }
}
