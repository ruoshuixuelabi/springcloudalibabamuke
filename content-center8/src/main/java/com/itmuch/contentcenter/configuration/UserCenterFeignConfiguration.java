package com.itmuch.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * 自定义Feign的日志级别，以Java代码的方式
 * 如果该类添加了@Configuration注解，
 * 就需要把该类移动到主启动类的包以及子包之外，这个其实还是前面学习的父子上下文问题
 * @author admin
 */
public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}