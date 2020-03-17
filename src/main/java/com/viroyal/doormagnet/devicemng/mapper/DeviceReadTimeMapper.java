package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.model.DeviceReadTime;

public interface DeviceReadTimeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insert(DeviceReadTime record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insertSelective(DeviceReadTime record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    DeviceReadTime selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKeySelective(DeviceReadTime record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_read_time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKey(DeviceReadTime record);
}