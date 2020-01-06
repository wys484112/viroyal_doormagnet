package com.viroyal.doormagnet.devicemng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.viroyal.doormagnet.util.ErrorCode;

/**
 * 绑定协议响应封装
 *
 * @author lijiuwei
 */
public class DataListResponse extends BaseResponse {

    @JsonProperty("extra")
    public ExtraInfo extra;

    public DataListResponse() {
        super();
    }

    public DataListResponse(Object dataList) {
        errorCode = ErrorCode.SUCCESS;
        extra = new ExtraInfo();
        extra.data = dataList;
    }

    public DataListResponse(Object dataList, int nextId) {
        this(dataList);
        extra.nextId = nextId;
    }

    static class ExtraInfo {
        public Object data;

        public Integer nextId;
    };
}
