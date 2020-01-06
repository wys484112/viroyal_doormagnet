package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true, value = {"upload_token", "bucket_name", "type", "domain"})
public class TokenStore implements Serializable {
    @JsonProperty("upload_token")
    private String uploadToken;

    @JsonProperty("bucket_name")
    private String bucketName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("domain")
    private String domain;

    public TokenStore(String type, String uploadToken, String bucketName, String domain) {
        this.type = type;
        this.uploadToken = uploadToken;
        this.bucketName = bucketName;
        this.domain = domain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
