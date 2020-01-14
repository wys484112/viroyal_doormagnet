package com.viroyal.doormagnet.devicemng.socket;

import com.viroyal.doormagnet.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.viroyal.doormagnet.util.TextUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 表示从设备收到的数据，但socket相关的消息也通过这个来传递。
 *
 * @author LiGang
 *
 */
public class DeviceMessage extends DeviceMessageBase {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8569740216219574569L;

	private static final Logger logger = LoggerFactory.getLogger(DeviceMessage.class);

    private Channel channel;
    private String imei;
    
    
	public DeviceMessage() {
		super();
		// TODO Auto-generated constructor stub
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
	@Override
	public String toString() {
		return "DeviceMessage [channel=" + channel + ", imei=" + imei + ", headHexStr=" + headHexStr + ", flagHexStr="
				+ flagHexStr + ", controlHexStr=" + controlHexStr + ", versionHexStr=" + versionHexStr
				+ ", contentLengthHexStr=" + contentLengthHexStr + ", contentHexStr=" + contentHexStr + ", endsHexStr="
				+ endsHexStr + "]";
	}

}
