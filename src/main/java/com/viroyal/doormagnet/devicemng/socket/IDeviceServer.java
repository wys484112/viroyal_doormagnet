package com.viroyal.doormagnet.devicemng.socket;

import java.util.List;

import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
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
	
	/**
	 * 获取当前在线的设备的imei
	 */	
	List<String> getDeviceActiveList();
	/**
	 * 通过imei获取设备的socket channel，方便与设备通讯
	 */	
	Channel  getChannelFromImei(String imei);
}
