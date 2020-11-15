package com.itmuch.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.itmuch.contentcenter.configuration.UserCenterFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author admin
 */
@MapperScan("com.itmuch.contentcenter.dao")
@SpringBootApplication
//@EnableFeignClients
@EnableFeignClients(defaultConfiguration = UserCenterFeignConfiguration.class)
public class ContentCenterApplication8 {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication8.class, args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}