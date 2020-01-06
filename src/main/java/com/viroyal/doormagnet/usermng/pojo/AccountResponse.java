package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@SuppressWarnings(value = "unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse extends BaseResponse implements Serializable {
    @JsonProperty("extra")
    private Object extra;

    public AccountResponse() {

    }

    public AccountResponse(Integer errorCode, String errorMessage, Object extra) {
        super(errorCode, errorMessage);
        this.extra = extra;
    }
}
