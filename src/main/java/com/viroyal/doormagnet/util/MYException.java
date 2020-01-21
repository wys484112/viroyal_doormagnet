package com.viroyal.doormagnet.util;

import java.util.HashMap;
import java.util.Map;

public  class MYException extends Exception{
	
	public static String SUCCESS="成功";
	public static String INVALID_PARAM="无效参数";
	public static String SYSTEM_BUSY="系统繁忙";
	public static String INVALID_TOKEN="无效的token";
	public static String SERVICE_SEND_ERROR="服务器发送失败";
	public static String DEVICE_RESPONSE_ERROR="设备回复失败";
	public static String DEVICE_NOT_ACTIVATED="设备不在线";
	public static String DEVICE_ALREADY_BINDED="设备以及绑定";
	public static String DEVICE_NOT_BINDED="设备没有绑定";	
	public static Map<String, Integer > exceptions = new HashMap<String, Integer>(){{  
	      put(SUCCESS,ErrorCode.SUCCESS);  
	      put(INVALID_PARAM,ErrorCode.INVALID_PARAM);      
	      put(SYSTEM_BUSY,ErrorCode.SYSTEM_BUSY);      
	      put(INVALID_TOKEN,ErrorCode.INVALID_TOKEN);      
	      put(SERVICE_SEND_ERROR,ErrorCode.SERVICE_SEND_ERROR);      
	      put(DEVICE_RESPONSE_ERROR,ErrorCode.DEVICE_RESPONSE_ERROR);      
	      put(DEVICE_NOT_ACTIVATED,ErrorCode.DEVICE_NOT_ACTIVATED);      
	      put(DEVICE_ALREADY_BINDED,ErrorCode.DEVICE_ALREADY_BINDED);      
	      put(DEVICE_NOT_BINDED,ErrorCode.DEVICE_NOT_BINDED);      

	}};
	public MYException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MYException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	public MYException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public MYException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public MYException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
