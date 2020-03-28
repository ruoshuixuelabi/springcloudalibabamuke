package com.itmuch.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义路由谓词工厂
 * 注意它一定要以RoutePredicateFactory结尾
 * 并且这里的TimeBetween要和配置文件里面配置的一致，比如这个案例
 * 配置文件里面配置的是TimeBetween=上午9:00,下午10:00。因此类名叫TimeBetweenRoutePredicateFactory
 * 注意需要加入@Component纳入容器管理，不然不会生效的
 *
 * @author admin
 */
@Component
public class TimeBetweenRoutePredicateFactory
        extends AbstractRoutePredicateFactory<TimeBetweenConfig> {
    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    /**
     * 谓词工厂的核心方法
     */
    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();
        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(start) && now.isBefore(end);
        };
    }

    /**
     * 指定顺序，
     * 比如这里指定的代表start是第一个参数，end是第二个参数
     * 对应配置文件 TimeBetween=上午9:00,下午10:00里面的上午9:00就是第一个参数下午10:00是第二个参数
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start", "end");
    }

    public static void main(String[] args) {
        //测试我们的spring cloud gateway支持的时间的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println(formatter.format(LocalTime.now()));
    }
}