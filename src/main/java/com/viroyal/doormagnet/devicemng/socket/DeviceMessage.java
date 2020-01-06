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

    public DeviceMessage(Channel channel, int what, ByteBuf data, DeviceBizHandler handler) {
        super(channel, what, handler);

        if (data != null) {
            mData = new byte[data.readableBytes()]; // 包含校验位
            data.readBytes(mData);
            logger.info(TextUtils.byte2HexStr(mData));

            byte input = mData[0];
            if (input != (byte)0xAA) {
                logger.error("not start with 0xAA, input={}, 0xaa={}", input, (byte)0xaa);
                mIsValid = false;
                return;
            }

            input = mData[1];
            int len = input - 1;
            if (len <= 0) {
                logger.error("invalid packet length:" + len);
                mIsValid = false;
                return;
            }

            byte checksum = Utils.calcChecksum(mData, 1, len + 1);
            if (checksum != mData[mData.length - 1]) {
                logger.error("invalid packet checksum, should be " + Integer.toHexString(checksum & 0xFF));
                mIsValid = false;
                return;
            }

            mPayload = new byte[len];
            System.arraycopy(mData, 2, mPayload, 0, len);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[what=");
        sb.append(mWhat);
        if (mData != null) {
            sb.append(" data=" + TextUtils.byte2HexStr(mData));
        }
        sb.append("]");

        return sb.toString();
    }
}
