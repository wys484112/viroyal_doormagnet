package com.viroyal.doormagnet.devicemng.model;

import java.util.Date;

public class DeviceReportCurrentAbnormal {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_currentabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_currentabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private String imei;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_currentabnormal.currentstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Byte currentstatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_device_report_currentabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    private Date time;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_currentabnormal.id
     *
     * @return the value of t_device_report_currentabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_currentabnormal.id
     *
     * @param id the value for t_device_report_currentabnormal.id
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_currentabnormal.imei
     *
     * @return the value of t_device_report_currentabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public String getImei() {
        return imei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_currentabnormal.imei
     *
     * @param imei the value for t_device_report_currentabnormal.imei
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_currentabnormal.currentstatus
     *
     * @return the value of t_device_report_currentabnormal.currentstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Byte getCurrentstatus() {
        return currentstatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_currentabnormal.currentstatus
     *
     * @param currentstatus the value for t_device_report_currentabnormal.currentstatus
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setCurrentstatus(Byte currentstatus) {
        this.currentstatus = currentstatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_device_report_currentabnormal.time
     *
     * @return the value of t_device_report_currentabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_device_report_currentabnormal.time
     *
     * @param time the value for t_device_report_currentabnormal.time
     *
     * @mbg.generated Tue Mar 17 14:21:33 CST 2020
     */
    public void setTime(Date time) {
        this.time = time;
    }
}