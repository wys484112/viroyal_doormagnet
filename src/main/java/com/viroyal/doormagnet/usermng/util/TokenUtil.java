package com.viroyal.doormagnet.usermng.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    private static final String UTF8 = "utf-8";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    /**
     * BASE64编码
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static final String base64Encoder(String src) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        // return (src == null) ? null :
        // Base64.encodeBase64String(md.digest(src.getBytes()));
        return new String(Base64.encodeBase64(src.getBytes()), "UTF-8");
        // return (src == null) ? null: (new
        // sun.misc.BASE64Encoder()).encode(src.getBytes(UTF8));
    }

    /**
     * BASE64解码
     *
     * @param dest
     * @return
     * @throws Exception
     */
    public static final String base64Decoder(String dest) throws Exception {
        return (dest == null) ? null : new String(Base64.decodeBase64(dest), UTF8);
    }

    /**
     * MD5数字签名
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static final String md5Digest(String src) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(src.getBytes(UTF8));
        return byte2HexStr(b);
    }

    private static final String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    private static final byte[] calculateMd5(byte[] binaryData) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
        messageDigest.update(binaryData);

        return messageDigest.digest();
    }

    public static final String getFileMD5(MultipartFile multipartFile) {
        try {
            byte[] binaryData = multipartFile.getBytes();
            byte[] md5Bytes = calculateMd5(binaryData);
            int len = md5Bytes.length;
            char buf[] = new char[len * 2];
            for (int i = 0; i < len; i++) {
                buf[i * 2] = HEX_DIGITS[(md5Bytes[i] >>> 4) & 0x0f];
                buf[i * 2 + 1] = HEX_DIGITS[md5Bytes[i] & 0x0f];
            }
            return new String(buf);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String getFileMD5(File file) {
        String value = null;
        try {
            FileInputStream in = new FileInputStream(file);
            try {
                MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(byteBuffer);
                BigInteger bi = new BigInteger(1, md5.digest());
                value = bi.toString(16);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return value.toUpperCase();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static final String token(String src) {
        if (src == null)
            return null;
        try {
            byte data[] = src.getBytes("UTF8");
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(data);
            byte resultData[] = m.digest();

            return convertToHexString(resultData);

        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private static String convertToHexString(byte data[]) {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            strBuffer.append(Integer.toHexString((0x000000FF & data[i]) | 0xFFFFFF00).substring(6));
        }

        return strBuffer.toString();
    }
}
