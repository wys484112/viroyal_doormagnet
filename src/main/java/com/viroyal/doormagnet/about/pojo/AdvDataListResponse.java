package com.viroyal.doormagnet.about.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viroyal.doormagnet.util.ErrorCode;

/**
 * 获取广告数据响应封装
 * 
 * @author walker
 *
 */
public class AdvDataListResponse extends BaseResponse {

    @JsonProperty("extra")
    public ExtraInfo extra;

    public AdvDataListResponse() {
        super();
    }

    public AdvDataListResponse(Object advDataList) {
        errorCode = ErrorCode.SUCCESS;
        extra = new ExtraInfo();
        extra.data = advDataList;
    }

    static class ExtraInfo {
        public Object data;
    }

}
