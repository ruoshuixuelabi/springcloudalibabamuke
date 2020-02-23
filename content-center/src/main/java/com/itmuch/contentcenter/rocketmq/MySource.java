package com.itmuch.contentcenter.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author admin
 */
public interface MySource {
    String MY_OUTPUT = "my-output";
    @Output(MY_OUTPUT)
    MessageChannel output();
}
