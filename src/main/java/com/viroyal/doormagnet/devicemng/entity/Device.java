package com.viroyal.doormagnet.devicemng.entity;

public class Device {
    public static final int STATUS_NOT_ACTIVATED = 0;
    public static final int STATUS_ACTIVATED = 2;
    public static final int STATUS_BINDED = 3;

    public int id;

    public String sn;

    public String name;

    public int status;

    public int online;
}
