package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.model.DeviceReportCurrentAbnormal;

public interface DeviceReportCurrentAbnormalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insert(DeviceReportCurrentAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int insertSelective(DeviceReportCurrentAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    DeviceReportCurrentAbnormal selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKeySelective(DeviceReportCurrentAbnormal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_device_report_currentabnormal
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    int updateByPrimaryKey(DeviceReportCurrentAbnormal record);
}