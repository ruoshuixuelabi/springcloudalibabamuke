package com.tumuch.boot.springbootdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}