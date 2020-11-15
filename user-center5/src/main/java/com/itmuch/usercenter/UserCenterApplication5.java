package com.itmuch.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author admin
 * 注解@MapperScan用来扫描mybatis哪些包里面的接口
 */
@MapperScan("com.itmuch.usercenter.dao")
@SpringBootApplication
public class UserCenterApplication5 {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication5.class, args);
    }
}