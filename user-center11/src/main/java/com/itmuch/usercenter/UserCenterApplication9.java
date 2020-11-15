package com.itmuch.usercenter;

import com.itmuch.usercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author admin
 * 注解@MapperScan用来扫描mybatis哪些包里面的接口
 */
@MapperScan("com.itmuch.usercenter.dao")
@SpringBootApplication
@EnableBinding({Sink.class, MySink.class})
public class UserCenterApplication9 {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication9.class, args);
    }
}