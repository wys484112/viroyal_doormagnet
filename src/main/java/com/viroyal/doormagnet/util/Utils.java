package com.viroyal.doormagnet.util;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class Utils {
    public static float bytesToFloat(byte[] data, int start) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        int pos = start + 3;
        buffer.put(data[pos--]);
        buffer.put(data[pos--]);
        buffer.put(data[pos--]);
        buffer.put(data[pos--]);
        buffer.flip();
        return buffer.getFloat();
    }

    public static short bytesToShort(byte[] data, int start) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(data, start, 2);
        buffer.flip();
        return buffer.getShort();
    }

    public static int bytesToInt(byte[] data, int start) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(data, start, 4);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte calcChecksum(byte[] data, int startPos, int dataLen) {
        byte checksum = 0;
        int pos = startPos;
        for (int i = 0; i < dataLen; i++) {
            checksum ^= data[pos++];
        }
        return checksum;
    }

    public static byte calcChecksum(ByteBuf data, int startPos, int endPos) {
        byte checksum = 0;
        for (int i = startPos; i < endPos; i++) {
            checksum ^= data.getByte(i);
        }
        return checksum;
    }
}
