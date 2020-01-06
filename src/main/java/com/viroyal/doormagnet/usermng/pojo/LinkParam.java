package com.viroyal.doormagnet.usermng.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
public class LinkParam {
    @JsonProperty("acc_name")
    public String accName;

    @JsonProperty("type")
    public Integer type;
}
