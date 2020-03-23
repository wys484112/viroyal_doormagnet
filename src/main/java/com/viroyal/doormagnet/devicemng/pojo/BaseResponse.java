package com.viroyal.doormagnet.devicemng.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viroyal.doormagnet.common.util.ErrorCode;
import com.viroyal.doormagnet.common.util.MYException;

/**
 * 通用响应封装
 *
 * @author lijiuwei
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    public static final BaseResponse SUCCESS = new BaseResponse(ErrorCode.SUCCESS, null);

    public static final BaseResponse INVALID_PARAM = new BaseResponse(ErrorCode.INVALID_PARAM, "参数无效");

    public static final BaseResponse SYSTEM_BUSY = new BaseResponse(ErrorCode.SYSTEM_BUSY, "系统忙");

    public static final BaseResponse INVALID_TOKEN = new BaseResponse(ErrorCode.INVALID_TOKEN, "您的登录信息已过期，请重新登录");

    @JsonProperty("error_code")
    public Integer errorCode;

    @JsonProperty("error_msg")
    public String errorMsg;

    public BaseResponse() {
    }

    public BaseResponse(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
	public BaseResponse(String errorMsg) {
		this.errorCode = MYException.exceptions.containsKey(errorMsg) ? MYException.exceptions.get(errorMsg)
				: ErrorCode.INVALID_PARAM;
		this.errorMsg = errorMsg;
	}
    

    public static BaseResponse getInvalidParamResponse(String errorMsg) {
        return new BaseResponse(ErrorCode.INVALID_PARAM, errorMsg);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return (this.errorCode == 1000);
    }

}
