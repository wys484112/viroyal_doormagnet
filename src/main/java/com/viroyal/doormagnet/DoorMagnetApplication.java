package com.viroyal.doormagnet;

import com.viroyal.doormagnet.devicemng.service.IDeviceGateway;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.viroyal.doormagnet.devicemng.mapper")
public class DoorMagnetApplication {
    private static final Logger Logger = LoggerFactory.getLogger(DoorMagnetApplication.class);

    @Autowired
    IDeviceGateway mGateway;

    public static void main(String[] args) {
        SpringApplication.run(DoorMagnetApplication.class);
    }

    @Bean
    CommandLineRunner startServer() {
        return args -> {
            Logger.info("startServer");
            mGateway.startup();
        };
    }
}
