package com.itmuch.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author admin
 */
@Slf4j
public class TestControllerBlockHandlerClass {
    /**
     * 处理限流或者降级
     */
    public static String block(String a, BlockException e) {
        log.warn("限流，或者降级了 block", e);
        return "限流，或者降级了 block";
    }
}