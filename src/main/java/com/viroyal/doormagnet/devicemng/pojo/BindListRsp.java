package com.viroyal.doormagnet.devicemng.pojo;

import java.sql.Timestamp;

/**
 * 获取绑定列表的响应
 */
public class BindListRsp {
    public int id;

    public String sn;

    public String name;

    public Timestamp alertTime;

    public Integer security;
}
