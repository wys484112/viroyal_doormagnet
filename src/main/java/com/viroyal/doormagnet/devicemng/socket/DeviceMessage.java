package com.viroyal.doormagnet.devicemng.socket;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

/**
 * 表示从设备收到的数据装配，服务器需要发送给设备的数据装配。
 *
 * @author LiGang
 *
 */
public class DeviceMessage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8569740216219574569L;

	private static final Logger logger = LoggerFactory.getLogger(DeviceMessage.class);

    private Channel channel;
    private String imei;
    
    
	// 报文头
	public String headHexStr;
   
	// 标志位
	public String flagHexStr;
	
	// 控制位
	public String controlHexStr;
    
	// 版本位   null：发送给设备的信息的装配。不为null则为发送给设备的信息
	public String versionHexStr=null;	

	// 数据区长度
	public String contentLengthHexStr;
    
	// 数据区内容
	public String contentHexStr;
    
	// 结束符
	public String endsHexStr;

	
	public DeviceMessage() {
		super();
		// TODO Auto-generated constructor stub
		this.headHexStr = "6F01";
		this.endsHexStr = "0D0A0D0A";		
	}

	public DeviceMessage(Channel channel, String imei, String headHexStr, String flagHexStr, String controlHexStr,
			String versionHexStr, String contentLengthHexStr, String contentHexStr, String endsHexStr) {
		super();
		this.channel = channel;
		this.imei = imei;
		this.headHexStr = headHexStr;
		this.flagHexStr = flagHexStr;
		this.controlHexStr = controlHexStr;
		this.versionHexStr = versionHexStr;
		this.contentLengthHexStr = contentLengthHexStr;
		this.contentHexStr = contentHexStr;
		this.endsHexStr = endsHexStr;
	}

	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public String getImei() {
		return imei;
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getHeadHexStr() {
		return headHexStr;
	}

	public void setHeadHexStr(String headHexStr) {
		this.headHexStr = headHexStr;
	}

	public String getFlagHexStr() {
		return flagHexStr;
	}

	public void setFlagHexStr(String flagHexStr) {
		this.flagHexStr = flagHexStr;
	}

	public String getControlHexStr() {
		return controlHexStr;
	}

	public void setControlHexStr(String controlHexStr) {
		this.controlHexStr = controlHexStr;
	}

	public String getVersionHexStr() {
		return versionHexStr;
	}

	public void setVersionHexStr(String versionHexStr) {
		this.versionHexStr = versionHexStr;
	}

	public String getContentLengthHexStr() {
		return contentLengthHexStr;
	}

	public void setContentLengthHexStr(String contentLengthHexStr) {
		this.contentLengthHexStr = contentLengthHexStr;
	}

	public String getContentHexStr() {
		return contentHexStr;
	}

	public void setContentHexStr(String contentHexStr) {
		this.contentHexStr = contentHexStr;
	}

	public String getEndsHexStr() {
		return endsHexStr;
	}

	public void setEndsHexStr(String endsHexStr) {
		this.endsHexStr = endsHexStr;
	}


	@Override
	public String toString() {
		if (versionHexStr != null) {
			return headHexStr + flagHexStr + controlHexStr + versionHexStr + contentLengthHexStr + contentHexStr
					+ endsHexStr;

		} else {
			return headHexStr + flagHexStr + controlHexStr + contentLengthHexStr + contentHexStr + endsHexStr;

		}
	}
}
