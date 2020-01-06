package com.viroyal.doormagnet.about.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viroyal.doormagnet.util.ErrorCode;

/**
 * 通用响应封装
 * 
 * @author walker
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    public static final BaseResponse SUCCESS = new BaseResponse(ErrorCode.SUCCESS, null);

    public static final BaseResponse SYSTEM_BUSY = new BaseResponse(ErrorCode.SYSTEM_BUSY, "系统忙");

    @JsonProperty("error_code")
    public Integer errorCode;

    @JsonProperty("error_msg")
    public String errorMsg;

    public BaseResponse() {
        super();
    }

    public BaseResponse(Integer errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return (this.errorCode == 1000);
    }

}
