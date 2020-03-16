package com.viroyal.doormagnet.devicemng.socket;

import java.util.List;

import org.springframework.util.concurrent.ListenableFuture;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceBrightness;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceInstallationstateAnglethreadhold;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceLightingStrategy;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceReportInterval;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceSwitch;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceTime;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;

import io.netty.channel.Channel;

/**
 * 设备Server socket接口类
 * @author LiGang
 *
 */
public interface IDeviceServer {
	/**
	 * 创建Server socket并侦听连接
	 */
	void startup() throws Exception;
	/**
	 * 关闭server socket
	 */
	void shutdown();
	
    void sendToDevice();	
	/**
	 * 获取当前在线的设备的imei
	 */	
	List<String> getDeviceActiveList();
	/**
	 * 通过imei获取设备的socket channel，方便与设备通讯
	 */	
	Channel  getChannelFromImei(String imei);
	
	
    //设置路灯3路灯的开关
    BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param) throws TokenInvalidException;

    //设置路灯3路灯的亮度
    BaseResponse setDeviceSettingBrightness(String token, String devId, ServiceSettingsDeviceBrightness param) throws TokenInvalidException;

    //设置路灯定时上报时间间隔
    BaseResponse setDeviceSettingReportInterval(String token, String devId, ServiceSettingsDeviceReportInterval param) throws TokenInvalidException;
    
    //设置路灯亮灯策略
    BaseResponse setDeviceSettingStrategy(String token, String devId, ServiceSettingsDeviceLightingStrategy param) throws TokenInvalidException;

    //设置路灯时间
    BaseResponse setDeviceSettingTime(String token, String devId, ServiceSettingsDeviceTime param) throws TokenInvalidException;

    //设置路灯重启
    BaseResponse setDeviceSettingReboot(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯CELLID
    BaseResponse getDeviceSettingCellId(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯软件版本号
    BaseResponse getDeviceSettingSoftVersion(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //查询路灯硬件版本号
    BaseResponse getDeviceSettingHardVersion(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置路灯安装状态及角度阀值
    BaseResponse setDeviceSettingInstallationstateAnglethreadhold(String token, String devId, ServiceSettingsDeviceInstallationstateAnglethreadhold param) throws TokenInvalidException;

    //查询耗电量
    BaseResponse getDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param) throws TokenInvalidException;

    //设置耗电量
    BaseResponse setDeviceSettingPowerConsumption(String token, String devId, DeviceSetting param) throws TokenInvalidException;
    
    //上传到数据库 设备状态信息
    BaseResponse saveDeviceStatus(String token, String devId, DeviceStatus param) throws TokenInvalidException;


    
}
