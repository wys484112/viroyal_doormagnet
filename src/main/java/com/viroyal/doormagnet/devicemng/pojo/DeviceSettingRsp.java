package com.viroyal.doormagnet.devicemng.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;

public class DeviceSettingRsp {
    public int security;

    public  int beep;

    public  int allDay;

    public Time  timeStart;

    public Time  timeEnd;

    public int appPush;

    public int pushSlient;

    public Time  slientStart;

    public Time  slientEnd;

    //绑定表里面的名称
    public String name;
    //电压(伏)
    public Integer voltage;
    //电量(百分比)
    public Integer power;

}
