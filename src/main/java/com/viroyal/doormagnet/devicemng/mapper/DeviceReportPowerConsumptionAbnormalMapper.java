package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.model.DeviceReportPowerConsumptionAbnormal;

public interface DeviceReportPowerConsumptionAbnormalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insert(DeviceReportPowerConsumptionAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insertSelective(DeviceReportPowerConsumptionAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    DeviceReportPowerConsumptionAbnormal selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKeySelective(DeviceReportPowerConsumptionAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_powerconsumptionabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKey(DeviceReportPowerConsumptionAbnormal record);
}