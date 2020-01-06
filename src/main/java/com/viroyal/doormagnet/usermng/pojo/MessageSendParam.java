package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MessageSendParam implements Serializable {
    @JsonProperty("phone")
    public String phone;

    @JsonProperty("action")
    public String action;

    @JsonProperty("type")
    public Integer type;
    @JsonProperty("param1")
    public String param1;

    @JsonProperty("param2")
    public String param2;

    @JsonProperty("param3")
    public String param3;

    @JsonProperty("param4")
    public String param4;

    public MessageSendParam(String phone, String action, int type) {
        this.phone = phone;
        this.action = action;
        this.type = type;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }
}
