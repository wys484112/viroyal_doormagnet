package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class MessageParam {
    @JsonProperty("phone")
    public String phone;

    @JsonProperty("sms_reason")
    public String smsReason;

    @JsonProperty("sms_type")
    public String smsType;

    public MessageParam(String phone, String smsReason, String smsType) {
        this.phone = phone;
        this.smsReason = smsReason;
        this.smsType = smsType;
    }
}
