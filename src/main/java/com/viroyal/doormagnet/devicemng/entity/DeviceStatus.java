package com.viroyal.doormagnet.devicemng.entity;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Time;
import java.sql.Timestamp;


/*
 * 
 * 
 *
 * 
 */
public class DeviceStatus {
	// 设备id
	public String imei;
	// 电压
	public int voltage;
	// 电流
	public int current;
	// 有功功率
	public int activePower;
	// 无功功率
	public int reactivePower;
	// 功率因数
	public int powerfactor;
	// 温度
	public int temperature;
	// 耗电量整数部分
	public int powerConsumptionIntegerPart;
	// 耗电量小数部分
	public int powerConsumptionDecimalPart;
	// 控制器1对应灯的亮度
	public int brightnessControlOne;
	// 控制器2对应灯的亮度
	public int brightnessControlTwo;
	// 控制器3对应灯的亮度
	public int brightnessControlThree;
	// 控制器1对应灯的开关
	public int switchControlOne;
	// 控制器2对应灯的开关
	public int switchControlTwo;
	// 控制器3对应灯的开关
	public int switchControlThree;
	// 信号强度的绝对值
	public int signalStrengthAbsoluteValue;
	// 调光方式
	public int dimmingMode;
	// 异常标志
	public char abnormalFlag;
	// 倾斜角度1
	public int angleOne;
	// 倾斜角度2
	public int angleTwo;
	// 设备初始倾斜角度1
	public int angleOriginalOne;
	// 设备初始倾斜角度2
	public int angleOriginalTwo;
}
