package com.itmuch.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.itmuch.contentcenter.configuration.UserCenterFeignConfiguration;
import com.itmuch.contentcenter.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author admin
 */
@MapperScan("com.itmuch.contentcenter.dao")
@SpringBootApplication
@EnableBinding({Source.class, MySource.class})
@EnableFeignClients(defaultConfiguration = UserCenterFeignConfiguration.class)
public class ContentCenterApplication9 {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication9.class, args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}