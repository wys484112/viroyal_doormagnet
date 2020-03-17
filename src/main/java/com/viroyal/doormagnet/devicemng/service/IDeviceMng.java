package com.viroyal.doormagnet.devicemng.service;

import com.viroyal.doormagnet.devicemng.entity.DeviceSetting;
import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.model.DeviceStatus;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceBrightness;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceInstallationstateAnglethreadhold;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceLightingStrategy;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDevicePowerConsumption;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceReportInterval;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceSwitch;
import com.viroyal.doormagnet.devicemng.model.ServiceSettingsDeviceTime;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;
import com.viroyal.doormagnet.devicemng.pojo.BindReqParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IDeviceMng {
	BaseResponse bind(String token, BindReqParam param) throws TokenInvalidException;

	BaseResponse unbind(String token, int devId) throws TokenInvalidException;

	// 获取所有的设备imei
	BaseResponse getDeviceList(String token) throws TokenInvalidException;

	// 获取在线设备
	BaseResponse getDeviceListActive(String token) throws TokenInvalidException;

	// 获取 设备状态信息
	BaseResponse getDeviceStatusList(String token, String imei) throws TokenInvalidException;

	// 上传到数据库 设备状态信息
	BaseResponse saveDeviceStatus(String token, String devId, DeviceStatus param) throws TokenInvalidException;

	BaseResponse getDeviceSetting(String token, int devId) throws TokenInvalidException;

	BaseResponse getDeviceAlert(String token, int devId, int nextId) throws TokenInvalidException;

	BaseResponse setDeviceSetting(String token, int devId, DeviceSetting param) throws TokenInvalidException;

	
	
	// 四、服务器控制查询设备指令
	// 5.1设置控制器1~3路灯开/关
	BaseResponse setDeviceSettingSwitch(String token, String devId, ServiceSettingsDeviceSwitch param)
			throws TokenInvalidException;

	// 5.2设置1~3路灯的亮度等级
	BaseResponse setDeviceSettingBrightness(String token, String devId, ServiceSettingsDeviceBrightness param)
			throws TokenInvalidException;

	// 5.3设置设备定时上报时间间隔
	BaseResponse setDeviceSettingReportInterval(String token, String devId, ServiceSettingsDeviceReportInterval param)
			throws TokenInvalidException;

	// 5.4设置1~3路灯的亮灯策略
	BaseResponse setDeviceSettingStrategy(String token, String devId, ServiceSettingsDeviceLightingStrategy param)
			throws TokenInvalidException;

	// 5.5 下发时间给设备
	BaseResponse setDeviceSettingTime(String token, String devId, ServiceSettingsDeviceTime param)
			throws TokenInvalidException;

	// 5.6下发重启命令
	BaseResponse setDeviceSettingReboot(String token, String devId) throws TokenInvalidException;

	// 5.7查询CELLID命令
	BaseResponse getDeviceSettingCellId(String token, String devId) throws TokenInvalidException;

	// 5.8 查询软件版本号
	BaseResponse getDeviceSettingSoftVersion(String token, String devId) throws TokenInvalidException;

	// 5.9 查询固件版本号
	BaseResponse getDeviceSettingHardVersion(String token, String devId) throws TokenInvalidException;

	// 5.10设置安装状态及角度阀值
	BaseResponse setDeviceSettingInstallationstateAnglethreadhold(String token, String devId,
			ServiceSettingsDeviceInstallationstateAnglethreadhold param) throws TokenInvalidException;

	// 5.11 查询耗电量
	BaseResponse getDeviceSettingPowerConsumption(String token, String devId) throws TokenInvalidException;

	// 5.12 设置耗电量
	BaseResponse setDeviceSettingPowerConsumption(String token, String devId,
			ServiceSettingsDevicePowerConsumption param) throws TokenInvalidException;

}
