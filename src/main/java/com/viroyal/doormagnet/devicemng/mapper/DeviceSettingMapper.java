package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DeviceSettingMapper {
    @Select("select * from t_device_setting where dev_id=#{0}")
    DeviceSetting findOne(int devId);
}
