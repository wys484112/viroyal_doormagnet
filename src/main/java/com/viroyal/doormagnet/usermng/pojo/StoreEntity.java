package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/24.
 * Comments:
 */
public class StoreEntity implements Serializable {
    @JsonProperty("upload_token")
    @SerializedName("upload_token")
    public String uploadToken;

    @JsonProperty("bucket_name")
    @SerializedName("bucket_name")
    public String bucketName;

    @JsonProperty("type")
    @SerializedName("type")
    public String type;

    @JsonProperty("domain")
    @SerializedName("domain")
    public String domain;
}
