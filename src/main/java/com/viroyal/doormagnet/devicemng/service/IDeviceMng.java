package com.viroyal.doormagnet.devicemng.service;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.model.DeviceSwitchSetting;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.devicemng.pojo.BindReqParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IDeviceMng {
    BaseResponse bind(String token, BindReqParam param) throws TokenInvalidException;

    BaseResponse unbind(String token, int devId) throws TokenInvalidException;

    //获取所有的设备imei
    BaseResponse getDeviceList(String token) throws TokenInvalidException;

    //获取在线设备
    BaseResponse getDeviceListActive(String token) throws TokenInvalidException;

    
    //设置路灯3路灯的开关
    BaseResponse setDeviceSettingSwitch(String token, String devId, DeviceSwitchSetting param) throws TokenInvalidException;

    //设置路灯3路灯的亮度
    BaseResponse setDeviceSettingBrightness(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯定时上报时间间隔
    BaseResponse setDeviceSettingReportInterval(String token, String devId, DeviceSetting param) throws TokenInvalidException;
    
    //设置路灯亮灯策略
    BaseResponse setDeviceSettingStrategy(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯时间
    BaseResponse setDeviceSettingTime(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯重启
    BaseResponse setDeviceSettingReboot(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯CELLID
    BaseResponse getDeviceSettingCellId(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯软件版本号
    BaseResponse getDeviceSettingSoftVersion(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯硬件版本号
    BaseResponse getDeviceSettingHardVersion(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯安装状态及角度阀值
    BaseResponse setDeviceSettingMountAndAngularThreshold(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询耗电量
    BaseResponse getDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置耗电量
    BaseResponse setDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param) throws TokenInvalidException;
    
    //上传到数据库 设备状态信息
    BaseResponse setDeviceStatus(String token, String devId, DeviceStatus param) throws TokenInvalidException;

    //获取 设备状态信息
    BaseResponse getDeviceStatusList(String token, String imei) throws TokenInvalidException;

    
    
    BaseResponse getDeviceSetting(String token, int devId) throws TokenInvalidException;

    BaseResponse getDeviceAlert(String token, int devId, int nextId) throws TokenInvalidException;

    BaseResponse setDeviceSetting(String token, int devId, DeviceSetting param) throws TokenInvalidException;
}
