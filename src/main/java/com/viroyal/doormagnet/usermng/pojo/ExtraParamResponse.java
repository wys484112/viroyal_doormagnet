package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 绑定协议响应封装
 *
 * @author lijiuwei
 */
@SuppressWarnings(value = "unused")
@JsonPropertyOrder({"error_code", "error_msg", "extra"})
public class ExtraParamResponse<T> extends BaseResponse {

    @JsonProperty("extra")
    public T extra;

    public ExtraParamResponse() {
        super();
    }

    public ExtraParamResponse(BaseResponse baseResponse, T extra) {
        super(baseResponse.getErrorCode(), baseResponse.getErrorMsg());
        this.extra = extra;
    }

    public ExtraParamResponse(T extra) {
        this(BaseResponse.SUCCESS, extra);
    }
}
