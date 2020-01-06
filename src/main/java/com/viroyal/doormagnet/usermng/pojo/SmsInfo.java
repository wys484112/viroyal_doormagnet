package com.viroyal.doormagnet.usermng.pojo;

public class SmsInfo {
    private String phone;

    private String code;

    private String key;

    private String action;

    public SmsInfo() {

    }

    public SmsInfo(String phone,
                   String code,
                   String key,
                   String action) {
        this.phone = phone;
        this.code = code;
        this.key = key;
        this.action = action;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}