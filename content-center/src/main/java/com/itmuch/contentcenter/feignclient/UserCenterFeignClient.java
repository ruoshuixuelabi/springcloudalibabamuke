package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.configuration.UserCenterFeignConfiguration;
import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import com.itmuch.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import com.itmuch.contentcenter.feignclient.fallbackfactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 使用Feign调用远程的接口
 * @author admin
 */
//@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
//@FeignClient(name = "user-center",fallback = UserCenterFeignClientFallback.class)
@FeignClient(name = "user-center",fallbackFactory = UserCenterFeignClientFallbackFactory.class)
public interface UserCenterFeignClient {
    /**
     * http://user-center/users/{id}
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);

    /**
     * 添加新的方法，来测试get多参数的支持
     * 注意这里如果没有@SpringQueryMap 注解是会报错的
     */
    @GetMapping("/q")
    UserDTO query(@SpringQueryMap UserDTO userDTO);
}
