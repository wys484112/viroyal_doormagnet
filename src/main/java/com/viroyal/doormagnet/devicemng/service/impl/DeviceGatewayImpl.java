package com.viroyal.doormagnet.devicemng.service.impl;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.viroyal.doormagnet.devicemng.service.IDeviceGateway;
import com.viroyal.doormagnet.devicemng.socket.IDeviceServer;

@Service
public class DeviceGatewayImpl implements IDeviceGateway {

    private static final Logger logger = LoggerFactory.getLogger(DeviceGatewayImpl.class);

    @Autowired
    private IDeviceServer mDeviceServer;

    @Autowired
    Environment mEnvironment;

    @Override
    public void startup() {
        new StartupThread().start();
    }

    @PreDestroy
    public void destroy() throws Exception {
        logger.info("destroy");
        stopServers();
    }

    private void startServers() {
        logger.info("mDeviceServers=" + mDeviceServer);

        if (mDeviceServer == null)
            return;

        try {
            IDeviceServer server = mDeviceServer;
            server.startup();
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    private void stopServers() {
        logger.info("mDeviceServers=" + mDeviceServer);

        if (mDeviceServer == null)
            return;

        try {
            IDeviceServer server = mDeviceServer;
            server.shutdown();
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    private class StartupThread extends Thread {
        @Override
        public void run() {
            logger.info("StartupThread run start, wait for 5s");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException,", e);
                return;
            }
            // 启动Socket服务
            startServers();

            logger.info("StartupThread run end");
        }
    }
    
    
    
}
