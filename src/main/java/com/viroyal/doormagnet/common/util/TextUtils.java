package com.viroyal.doormagnet.common.util;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    private final static char[] mChars = "0123456789ABCDEF".toCharArray();

    public static boolean isEmpty(String value) {
        return (value == null || value.length() == 0) ? true : false;
    }

    
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	
    public static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int iLen = b.length;
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2Str(byte[] b) {
        return new String(b);
    }
    
    public static byte[] str2Byte(String src) {
        return src.getBytes();
    }
    
    public static String byte2HexStr(ByteBuf buf) {
        StringBuilder sb = new StringBuilder();
        int iLen = buf.readableBytes();
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(buf.getByte(n) & 0xFF) >> 4]);
            sb.append(mChars[buf.getByte(n) & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2HexStrNoSpace(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int iLen = b.length;
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static String byte2HexStrNoSpace(byte[] b, int start, int length) {
        StringBuilder sb = new StringBuilder();
        for (int n = start; n < start + length; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    public static byte[] hexStr2Bytes(String src) {
        /* 对输入值进行规范化整理 */
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        // 处理值初始化
        int m = 0, n = 0;
        int iLen = src.length() / 2; // 计算长度
        byte[] ret = new byte[iLen]; // 分配存储空间

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }
    
    /**
     * 	     	String src="383637";    	
     * 			String asciiStr="867";  
     * 
     * @param src
     * @return
     */
    public static String hexStr2AscIIStr(String src) {
        /* 对输入值进行规范化整理 */
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);

        StringBuilder asciiStr = new StringBuilder();
        for(int i=0;i<src.length()-1;i+=2) {
            String output = src.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            asciiStr.append((char)decimal);
        }
        return asciiStr.toString();
    }
    
    /**
     * 对象转byte
     * @param obj
     * @return
     */
    public static byte[] ObjectToByte(Object obj) {  
        byte[] bytes = null;  
        try {  
            // object to bytearray  
            ByteArrayOutputStream bo = new ByteArrayOutputStream();  
            ObjectOutputStream oo = new ObjectOutputStream(bo);  
            oo.writeObject(obj);  
      
            bytes = bo.toByteArray();  
      
            bo.close();  
            oo.close();  
        } catch (Exception e) {  
            System.out.println("translation" + e.getMessage());  
            e.printStackTrace();  
        }  
        return bytes;  
    } 
    
    /**
     * byte转对象
     * @param bytes
     * @return
     */
    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
    
}
