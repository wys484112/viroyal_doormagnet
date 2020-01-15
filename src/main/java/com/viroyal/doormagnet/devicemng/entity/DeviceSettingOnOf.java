package com.viroyal.doormagnet.devicemng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Time;

public class DeviceSettingOnOf {
	//设备id
	public String imei;
	//电压
	public Integer voltage;
	//电流
	public Integer current;
	//有功功率
	public Integer activePower;
	//无功功率
	public Integer reactivePower;
	//功率因数
	public Integer powerfactor;
	//温度
	public Integer temperature;
	//耗电量整数部分
	public Integer powerConsumptionIntegerPart;
	//耗电量小数部分
	public Integer powerConsumptionDecimalPart;
	//控制器1对应灯的亮度
	public Integer brightnessControlOne;
	//控制器2对应灯的亮度
	public Integer brightnessControlTwo;
	//控制器3对应灯的亮度
	public Integer brightnessControlThree;
	//控制器1对应灯的开关
	public Integer switchControlOne;
	//控制器2对应灯的开关
	public Integer switchControlTwo;
	//控制器3对应灯的开关
	public Integer switchControlThree;
	//信号强度的绝对值
	public Integer signalStrengthAbsoluteValue;
	//调光方式
	public Integer dimmingMode;
	//异常标志
	public char abnormalFlag;
	//倾斜角度1
	public Integer angleOne;
	//倾斜角度2
	public Integer angleTwo;
	//设备初始倾斜角度1
	public Integer angleOriginalOne;
	//设备初始倾斜角度2
	public Integer angleOriginalTwo;
}
