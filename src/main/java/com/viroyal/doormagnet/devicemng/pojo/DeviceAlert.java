package com.viroyal.doormagnet.devicemng.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class DeviceAlert {
    public int id;

    public int devId;

    public int action;

    public Timestamp actionTime;
}
