package com.viroyal.doormagnet.devicemng.model;

import java.util.Date;

public class DeviceReportLightAbnormal {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_lightabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_lightabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private String imei;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_lightabnormal.lightstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Byte lightstatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_lightabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Date time;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_lightabnormal.id
     *
     * @return the value of t_device_report_lightabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_lightabnormal.id
     *
     * @param id the value for t_device_report_lightabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_lightabnormal.imei
     *
     * @return the value of t_device_report_lightabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public String getImei() {
        return imei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_lightabnormal.imei
     *
     * @param imei the value for t_device_report_lightabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_lightabnormal.lightstatus
     *
     * @return the value of t_device_report_lightabnormal.lightstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Byte getLightstatus() {
        return lightstatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_lightabnormal.lightstatus
     *
     * @param lightstatus the value for t_device_report_lightabnormal.lightstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setLightstatus(Byte lightstatus) {
        this.lightstatus = lightstatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_lightabnormal.time
     *
     * @return the value of t_device_report_lightabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_lightabnormal.time
     *
     * @param time the value for t_device_report_lightabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setTime(Date time) {
        this.time = time;
    }
}