package com.viroyal.doormagnet.about.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * 响应封装
 * 
 * @author walker
 *
 */
@JsonPropertyOrder({ "error_code", "error_msg", "extra" })
public class ExtraParamResponse extends BaseResponse {

    @JsonProperty("extra")
    public Object extra;

    public ExtraParamResponse() {
        super();
    }

    public ExtraParamResponse(BaseResponse baseResponse, Object extra) {
        super(baseResponse.errorCode, baseResponse.errorMsg);
        this.extra = extra;
    }

    public ExtraParamResponse(Object extra) {
        this(BaseResponse.SUCCESS, extra);
    }

}
