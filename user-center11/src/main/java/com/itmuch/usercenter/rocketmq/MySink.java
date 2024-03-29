package com.itmuch.usercenter.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author admin
 */
public interface MySink {
    String MY_INPUT = "my-input";

    @Input(MY_INPUT)
    SubscribableChannel input();
}
