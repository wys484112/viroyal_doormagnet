package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.entity.DeviceOpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceOpLogMapper {
    @Insert("INSERT INTO t_device_op_log (dev_id, action, msg, action_time) " +
            "VALUES(#{devId}, #{action}, #{msg}, #{actionTime})")
    int insertOne(DeviceOpLog entity);
}
