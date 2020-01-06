package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Created by qinyl.
 * @date: 2017/3/2.
 * @comments:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushParam {
    @JsonProperty("registration_id")
    public String registrationId;
}
