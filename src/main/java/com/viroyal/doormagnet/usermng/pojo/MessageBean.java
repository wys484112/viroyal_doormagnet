package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class MessageBean /*implements Serializable*/ {
    @JsonProperty("phone")
    public String phone;

    @JsonProperty("action")
    public String action;

    @JsonProperty("type")
    public int type;

    @JsonProperty("code")
    public String code;

    @JsonProperty("key")
    public String key;

    @JsonProperty("expiration")
    public int expiration;

    public MessageBean() {

    }

    public MessageBean(String phone, String action, int type) {
        this.phone = phone;
        this.action = action;
        this.type = type;
    }

    public MessageBean(String phone, String action, String code, String key) {
        this.phone = phone;
        this.action = action;
        this.code = code;
        this.key = key;
    }
}
