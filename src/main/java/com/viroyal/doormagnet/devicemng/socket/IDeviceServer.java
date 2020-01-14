package com.viroyal.doormagnet.devicemng.socket;

import java.util.List;

import com.viroyal.doormagnet.devicemng.exception.TokenInvalidException;
import com.viroyal.doormagnet.devicemng.pojo.BaseResponse;

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
	public  List<String> getDeviceActiveList();

}
