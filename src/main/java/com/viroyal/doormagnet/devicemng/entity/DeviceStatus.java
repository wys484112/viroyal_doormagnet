package com.viroyal.doormagnet.devicemng.entity;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Time;
import java.sql.Timestamp;

public class DeviceStatus {
    public int devId;
    public int  openStatus;
    public Timestamp openStatusTime;
    public int chargeStatus;
    public Timestamp chargeStatusTime;
    //告警时间，只有判断为告警才设置
    public Timestamp alertTime;
    //已伏为单位的电压值
    public int voltage;
    //换算后的电量百分比
    public int power;
    //设防状态 1-开启LED和声音 2-只开LED 255-不开
    public int seurity;
    //设防开始时间
    public Time timeStart;
    //设防结束时间
    public Time timeEnd;
    //上传间隔 单位小时
    public int reportInterval;
    //报文序号
    public int lastSequence;
}
