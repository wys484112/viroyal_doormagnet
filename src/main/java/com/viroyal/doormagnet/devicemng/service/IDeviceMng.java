package com.viroyal.doormagnet.devicemng.service;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.devicemng.pojo.BindReqParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IDeviceMng {
    BaseResponse bind(String token, BindReqParam param) throws TokenInvalidException;

    BaseResponse unbind(String token, int devId) throws TokenInvalidException;

    BaseResponse getDeviceList(String token) throws TokenInvalidException;

    //设置路灯3路灯的开关
    BaseResponse setDeviceSettingSwitch(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯3路灯的亮度
    BaseResponse setDeviceSettingBrightness(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯定时上报时间间隔
    BaseResponse setDeviceSettingReportInterval(String token, int devId, DeviceSetting param) throws TokenInvalidException;
    
    //设置路灯亮灯策略
    BaseResponse setDeviceSettingStrategy(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯时间
    BaseResponse setDeviceSettingTime(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯重启
    BaseResponse setDeviceSettingReboot(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯CELLID
    BaseResponse getDeviceSettingCellId(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯软件版本号
    BaseResponse getDeviceSettingSoftVersion(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯硬件版本号
    BaseResponse getDeviceSettingHardVersion(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯安装状态及角度阀值
    BaseResponse setDeviceSettingMountAndAngularThreshold(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //查询耗电量
    BaseResponse getDeviceSettingPowerConsumption(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    //设置耗电量
    BaseResponse setDeviceSettingPowerConsumption(String token, int devId, DeviceSetting param) throws TokenInvalidException;

    
    BaseResponse getDeviceSetting(String token, int devId) throws TokenInvalidException;

    BaseResponse getDeviceAlert(String token, int devId, int nextId) throws TokenInvalidException;

    BaseResponse setDeviceSetting(String token, int devId, DeviceSetting param) throws TokenInvalidException;
}
