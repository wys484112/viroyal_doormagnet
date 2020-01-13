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
    private static final Logger logger = LoggerFactory.getLogger(DeviceMessage.class);

    // 是长度字段之后的数据, 不包含校验位
    public byte[] mPayload = null;
    public DeviceMessage(Channel channel, int what, byte[] data, DeviceBizHandler handler) {
        super(channel, what, handler);

        if (data != null) {
            mData = new byte[data.length]; // 包含校验位
            System.arraycopy(data, 0, mData, 0, data.length);
            logger.info(TextUtils.byte2HexStr(mData));
            
            String msg=TextUtils.byte2Str(mData);
            if(msg.contains("imei")) {
            	imei=msg.substring(5);
            }
         
        }
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[what=");
        sb.append(mWhat);
        if (mData != null) {
            sb.append(" data=" + TextUtils.byte2Str(mData));
        }
        sb.append("]");

        return sb.toString();
    }
}
