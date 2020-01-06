package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse extends BaseResponse {
    public Integer error_code;

    public String error_msg;

    @JsonProperty("extra")
    public SmsExtra extra;

    public SmsExtra getExtra() {
        return extra;
    }

    public void setExtra(SmsExtra extra) {
        this.extra = extra;
    }
}
