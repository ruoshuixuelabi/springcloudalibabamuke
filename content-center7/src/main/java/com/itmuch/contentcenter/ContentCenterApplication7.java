package com.itmuch.contentcenter;

import com.itmuch.contentcenter.configuration.UserCenterFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author admin
 */
@MapperScan("com.itmuch.contentcenter.dao")
@SpringBootApplication
//@EnableFeignClients
@EnableFeignClients(defaultConfiguration = UserCenterFeignConfiguration.class)
public class ContentCenterApplication7 {
    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication7.class, args);
    }
}