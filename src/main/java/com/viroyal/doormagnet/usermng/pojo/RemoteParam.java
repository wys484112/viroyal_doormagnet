package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder(alphabetic = true, value = {"phone", "sms_reason", "sms_type"})
public class RemoteParam implements Serializable {
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("sms_reason")
    private String sms_reason;

    @JsonProperty("sms_type")
    private Integer sms_type;

    public RemoteParam() {

    }

    public RemoteParam(String phone, String sms_reason, Integer sms_type) {
        this.phone = phone;
        this.sms_reason = sms_reason;
        this.sms_type = sms_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSms_reason() {
        return sms_reason;
    }

    public void setSms_reason(String sms_reason) {
        this.sms_reason = sms_reason;
    }

    public Integer getSms_type() {
        return sms_type;
    }

    public void setSms_type(Integer sms_type) {
        this.sms_type = sms_type;
    }
}
