package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceBrightness;

public interface ServiceSettingsDeviceBrightnessMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int insert(ServiceSettingsDeviceBrightness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int insertSelective(ServiceSettingsDeviceBrightness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    ServiceSettingsDeviceBrightness selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int updateByPrimaryKeySelective(ServiceSettingsDeviceBrightness record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_brightness
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int updateByPrimaryKey(ServiceSettingsDeviceBrightness record);
}