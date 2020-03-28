package com.itmuch.usercenter.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * 测试整合spring-cloud-starter-stream-rocketmq之后的接收消息功能
 *
 * @author admin
 */
@Service
@Slf4j
public class MyTestStreamConsumer {
    @StreamListener(MySink.MY_INPUT)
    public void receive(String messgaeBody) {
        log.info("自定义接口通过stream接收到了消息:messageBody={}", messgaeBody);
    }
}
