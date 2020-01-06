package com.viroyal.doormagnet.devicemng.entity;

import java.sql.Timestamp;

public class DeviceOpLog {
    public static final int ACTION_REGISTER = 0;
    public static final int ACTION_OPEN = 1;
    public static final int ACTION_CLOSE = 2;
    public static final int ACTION_CHARGE_STARAT = 3;
    public static final int ACTION_CHARGE_FINISH = 4;
    public static final int ACTION_STATUS_REPORT = 5;
    public static final int ACTION_OFFLINE = 6;
    public static final int ACTION_LOW_POWER = 7;
    public static final int ACTION_ONLINE = 8;
    public static final int ACTION_BINDED = 20;
    public static final int ACTION_UN_BINDED = 21;

    public long id;

    public int devId;

    public int action;

    public String msg;

    public Timestamp actionTime;
}
