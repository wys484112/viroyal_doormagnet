package com.viroyal.doormagnet.devicemng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Time;

public class DeviceSetting {
    public Integer devId;

    public Integer security;

    public Integer beep;

    public Integer allDay;

    public Time timeStart;

    public Time timeEnd;

    public Integer reportInterval;

    public Integer appPush;

    public Integer pushSlient;

    public Time slientStart;

    public Time slientEnd;

    public Long userId;
    //绑定表里的名称
    public String name;
}
