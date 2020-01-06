package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通用响应封装
 *
 * @author lijiuwei
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    public static final BaseResponse SUCCESS = new BaseResponse(1000, "");

    public static final BaseResponse SYSTEM_BUSY = new BaseResponse(1002, null);

    @JsonProperty("error_code")
    protected Integer errorCode;

    @JsonProperty("error_msg")
    protected String errorMsg;

    public BaseResponse() {
    }

    public BaseResponse(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseResponse(BaseResponse obj) {
        this.errorCode = obj.errorCode;
        this.errorMsg = obj.errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return (this.errorCode == 1000);
    }

    @Override
    public String toString() {
        return "[" + this.errorCode + "," + this.errorMsg + "]";
    }

    protected String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        String str = null;
        try {
            str = new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof BaseResponse)) {
            return false;
        }

        if (((BaseResponse) obj).getErrorCode() != this.getErrorCode()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return errorCode;
    }
}
