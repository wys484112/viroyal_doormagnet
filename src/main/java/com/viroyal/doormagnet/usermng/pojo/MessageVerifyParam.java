package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MessageVerifyParam implements Serializable {
    @JsonProperty("phone")
    public String phone;

    @JsonProperty("action")
    public String action;

    @JsonProperty("code")
    public String code;

    @JsonProperty("key")
    public String key;

    public MessageVerifyParam(String phone, String action, String code, String key) {
        this.phone = phone;
        this.action = action;
        this.code = code;
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
