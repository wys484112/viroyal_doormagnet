package com.viroyal.doormagnet.devicemng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 绑定协议响应封装
 *
 * @author lijiuwei
 */
@JsonPropertyOrder({"error_code", "error_msg", "extra"})
public class ExtraResponse extends BaseResponse {

    @JsonProperty("extra")
    public Object extra;

    public ExtraResponse() {
        super();
    }

    public ExtraResponse(BaseResponse baseResponse, Object extra) {
        super(baseResponse.errorCode, baseResponse.errorMsg);
        this.extra = extra;
    }

    public ExtraResponse(Object extra) {
        this(BaseResponse.SUCCESS, extra);
    }
}
