package com.viroyal.doormagnet.devicemng.mapper;

import org.apache.ibatis.annotations.Param;

import com.viroyal.doormagnet.devicemng.model.DeviceMessage;

public interface DeviceMessageMapper {
	
	int deleteByImeiAndControl(@Param("imei")String imei, @Param("controlhexstr")String controlhexstr);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    int insert(DeviceMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    int insertSelective(DeviceMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    DeviceMessage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    int updateByPrimaryKeySelective(DeviceMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_message
     *
     * @mbg.generated Sun Jan 19 15:45:57 CST 2020
     */
    int updateByPrimaryKey(DeviceMessage record);
}