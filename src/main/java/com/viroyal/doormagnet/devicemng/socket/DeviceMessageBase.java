package com.viroyal.doormagnet.devicemng.socket;

import io.netty.channel.Channel;

/**
 * 表示从设备收到的数据，但socket相关的消息也通过这个来传递。
 *
 * @author LiGang
 *
 */
public class DeviceMessageBase {
    public static final int MSG_SOCKET_CLOSED = 1;
    public static final int MSG_READ_IDLE = 2;
    public static final int MSG_READ_DATA = 100;

    public Channel mChannel;

    public int mDevId;

    public String  imei;
    
    public DeviceBizHandler mHandler;

    public int mWhat; // 参考MSG_READ_IDLE

    public Channel getmChannel() {
		return mChannel;
	}

	public void setmChannel(Channel mChannel) {
		this.mChannel = mChannel;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	// 设备消息内容
    public byte[] mData;

    // 这个消息是否有效
    public boolean mIsValid = true;

    public DeviceMessageBase(Channel channel, int what, DeviceBizHandler handler) {
        mChannel = channel;
        mWhat = what;
        mHandler = handler;
    }
}
