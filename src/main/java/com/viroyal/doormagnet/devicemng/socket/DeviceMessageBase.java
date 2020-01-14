package com.viroyal.doormagnet.devicemng.socket;

import java.io.Serializable;
import java.util.Arrays;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

/**
 * 表示从设备收到的数据，但socket相关的消息也通过这个来传递。
 *
 * @author LiGang
 *
 */
public class DeviceMessageBase implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = -2928533155615113557L;

	// 报文头
	public String headHexStr;
   
	// 标志位
	public String flagHexStr;
	
	// 控制位
	public String controlHexStr;
    
	// 版本位
	public String versionHexStr;	

	// 数据区长度
	public String contentLengthHexStr;
    
	// 数据区内容
	public String contentHexStr;
    
	// 结束符
	public String endsHexStr;

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
    
   
 
}
