package com.viroyal.doormagnet.devicemng.mapper;

import com.viroyal.doormagnet.devicemng.entity.DeviceStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DeviceStatusMapper {
    @Select("select last_sequence, voltage, alert_time from t_device_status where dev_id=#{0}")
    DeviceStatus findLastSequence(int devId);

    @Update("update t_device_status set open_status=#{openStatus}, open_status_time=#{openStatusTime}," +
            "alert_time=#{alertTime}, voltage=#{voltage}, power=#{power}, seurity=#{seurity}, time_start=#{timeStart}," +
            "time_end=#{timeEnd}, report_interval=#{reportInterval}, last_sequence=#{lastSequence}" +
            " where dev_id=#{devId}")
    int saveOpenStatusChanged(DeviceStatus statusEntity);

    @Insert("insert into t_device_status(dev_id, open_status, open_status_time, alert_time,voltage,power, seurity, time_start, time_end, " +
            "report_interval, last_sequence) " +
            "values(#{devId}, #{openStatus}, #{openStatusTime}, #{alertTime}, #{voltage},#{power}, #{seurity}, #{timeStart}, #{timeEnd}, " +
            "#{reportInterval}, #{lastSequence})")
    int insertOpenStatusChanged(DeviceStatus statusEntity);


    @Update("update t_device_status set charge_status=#{chargeStatus}, charge_status_time=#{chargeStatusTime}," +
            "voltage=#{voltage}, power=#{power}, seurity=#{seurity}, time_start=#{timeStart}," +
            "time_end=#{timeEnd}, report_interval=#{reportInterval}, last_sequence=#{lastSequence}" +
            " where dev_id=#{devId}")
    int saveChargeStatusChanged(DeviceStatus statusEntity);

    @Insert("insert into t_device_status(dev_id, charge_status, charge_status_time, voltage,power, seurity, time_start, time_end, " +
            "report_interval, last_sequence) " +
            "values(#{devId}, #{chargeStatus}, #{chargeStatusTime}, #{voltage}, #{power}, #{seurity}, #{timeStart}, #{timeEnd}, " +
            "#{reportInterval}, #{lastSequence})")
    int insertChargeStatusChanged(DeviceStatus statusEntity);

    @Update("update t_device_status set " +
            "voltage=#{voltage}, power=#{power}, seurity=#{seurity}, time_start=#{timeStart}," +
            "time_end=#{timeEnd}, report_interval=#{reportInterval}, last_sequence=#{lastSequence}" +
            " where dev_id=#{devId}")
    int saveBasicStatusChanged(DeviceStatus statusEntity);

    @Insert("insert into t_device_status(dev_id, voltage,power, seurity, time_start, time_end, " +
            "report_interval, last_sequence) " +
            "values(#{devId}, #{voltage}, #{power}, #{seurity}, #{timeStart}, #{timeEnd}, " +
            "#{reportInterval}, #{lastSequence})")
    int insertBasicStatusChanged(DeviceStatus statusEntity);

    //查询离线时间太长的记录
    @Select("SELECT id FROM t_device d  WHERE d.online=1 and d.online_time < TIMESTAMPADD(MINUTE,-10,TIMESTAMPADD(HOUR,-6, NOW()))")
    List<Integer> findOfflineDevices();
}
