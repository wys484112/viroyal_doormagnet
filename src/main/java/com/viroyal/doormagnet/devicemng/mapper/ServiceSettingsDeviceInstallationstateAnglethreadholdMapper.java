package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceInstallationstateAnglethreadhold;

public interface ServiceSettingsDeviceInstallationstateAnglethreadholdMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int insert(ServiceSettingsDeviceInstallationstateAnglethreadhold record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int insertSelective(ServiceSettingsDeviceInstallationstateAnglethreadhold record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    ServiceSettingsDeviceInstallationstateAnglethreadhold selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int updateByPrimaryKeySelective(ServiceSettingsDeviceInstallationstateAnglethreadhold record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_service_settings_device_installationstate_anglethreadhold
     *
     * @mbg.generated Mon Mar 16 14:36:40 CST 2020
     */
    int updateByPrimaryKey(ServiceSettingsDeviceInstallationstateAnglethreadhold record);
}