package com.itmuch.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * 自定义Feign的日志级别，以Java代码的方式
 * 这个类不要添加@Configuration 注解了，
 * 不然需要把这个类所在的包移动到主启动类能扫描到的包之外
 * @author admin
 */
public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level(){
        return  Logger.Level.FULL;
    }
}
