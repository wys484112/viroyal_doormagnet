package com.viroyal.doormagnet;

import com.viroyal.doormagnet.devicemng.service.IDeviceGateway;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableAsync //开启异步调用
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
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("Door-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
    
}
