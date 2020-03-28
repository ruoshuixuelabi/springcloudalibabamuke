package com.itmuch.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 实际的项目中根本不建议把来源参数放入请求的参数里面
 * 建议的是放入请求头
 *
 * @author admin
 */ //@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 从请求参数中获取名为 origin 的参数并返回，如果获取不到origin参数，那么就抛异常
        String origin = request.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
