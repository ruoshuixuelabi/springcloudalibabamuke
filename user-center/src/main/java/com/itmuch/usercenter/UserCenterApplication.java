package com.itmuch.usercenter;

import com.itmuch.usercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @MapperScan ：扫描mybatis哪些包里面的接口
 * @author admin
 */
@MapperScan("com.itmuch.usercenter.dao")
@SpringBootApplication
//@EnableDiscoveryClient
@EnableBinding({Sink.class, MySink.class})
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
