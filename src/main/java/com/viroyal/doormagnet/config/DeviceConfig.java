package com.viroyal.doormagnet.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DeviceConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Value("${jpush.APP_KEY}")
    private String JPUSH_APP_KEY;

    @Value("${jpush.MASTER_SECRET}")
    private String JPUSH_MASTER_SECRET;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return new Jackson2ObjectMapperBuilderCustomizer() {

            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
                jacksonObjectMapperBuilder.failOnUnknownProperties(false);
                jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            }
        };
    }



    @Bean
    public JPushClient createJpushClient() {
        logger.info("JPUSH_APP_KEY={}, JPUSH_MASTER_SECRET={}", JPUSH_APP_KEY, JPUSH_MASTER_SECRET);
        ClientConfig clientConfig = ClientConfig.getInstance();
        final JPushClient jpushClient = new JPushClient(JPUSH_MASTER_SECRET, JPUSH_APP_KEY, null, clientConfig);
        return jpushClient;
    }
}