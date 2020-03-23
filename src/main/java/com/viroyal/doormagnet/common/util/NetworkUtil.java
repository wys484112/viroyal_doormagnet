package com.viroyal.doormagnet.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtil {
    private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        logger.info("osName=" + osName);
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            }
            // 如果是Linux操作系统
            else {
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = netInterfaces.nextElement();
                    // ----------特定情况，可以考虑用ni.getName判断
                    // 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        ip = ips.nextElement();
                        logger.info("ip = " + ip);
                        if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
                                && ip.getHostAddress().indexOf(":") == -1
                                && ip.getHostAddress().startsWith("192.") == false) {
                            bFindIP = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("" + e.getMessage());
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
            logger.info("sIP = " + sIP);
        }
        return sIP;
    }
}
