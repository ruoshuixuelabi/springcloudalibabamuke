package com.itmuch.contentcenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.rocketmq.MySource;
import com.itmuch.contentcenter.sentineltest.TestControllerBlockHandlerClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@RestController
public class TestController {
    @Autowired(required = false)
    private ShareMapper shareMapper;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test")
    public List<Share> testInsert() {
        // 1. 做插入
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("xxx");
        share.setCover("xxx");
        share.setAuthor("大目");
        share.setBuyCount(1);
        this.shareMapper.insertSelective(share);
        // 2. 做查询: 查询当前数据库所有的share  select * from share ;
        return this.shareMapper.selectAll();
    }

    /**
     * 测试：服务发现，证明内容中心总能找到用户中心
     *
     * @return 用户中心所有实例的地址信息
     */
    @GetMapping("/test2")
    public List<ServiceInstance> getInstances() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...  即使不适用nacos使用这些也可以调用到
        return this.discoveryClient.getInstances("user-center");
    }

    /**
     * 测试热点规则的端点
     */
    @GetMapping("/test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a, @RequestParam(required = false) String b) {
        return a + " " + b;
    }

    @GetMapping("/test-sentinel-api")
    public String testSentinelApi(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName, "wfw");
        Entry entry = null;
        try {
            //定义一个Sentinel保护的资源
            entry = SphU.entry(resourceName);
            //被保护的业务逻辑
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        } catch (BlockException e) {
            //如果被保护的资源限流或者降级了，就会抛出BlockException
            return "限流，或者降级了";
        } catch (IllegalArgumentException e) {
            //统计IllegalArgumentException。默认情况下只会统计BlockException
            Tracer.trace(e);
            return "参数非法！";
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-api-annotation")
    @SentinelResource(value = "test-sentinel-api-annotation",
            blockHandler = "block", fallback = "fallback", blockHandlerClass = TestControllerBlockHandlerClass.class)
    public String testSentinelApiAnnotation(@RequestParam(required = false) String a) {
        //被保护的业务逻辑
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a不能为空");
        }
        return a;
    }

    @Autowired
    private  RestTemplate restTemplate;

    @GetMapping("/test-sentinel-api-restTemplate/{userId}")
    public UserDTO test(@PathVariable Integer userId) {
        return restTemplate.getForObject("http://user-center/users/{id}", UserDTO.class, userId);
    }

    public static String fallback(String a, Throwable e) {
        log.error("限流，或者降级了fallback", e);
        return "限流，或者降级了 fallback";
    }

    @Autowired
    private Source source;
    @GetMapping("/test-stream")
    public String testStream() {
        source.output().send(MessageBuilder.withPayload("消息体").build());
        return "success";
    }
    @Autowired
    private MySource mySource;
    @GetMapping("/test-stream2")
    public String testStream2() {
        mySource.output().send(MessageBuilder.withPayload("消息体").build());
        return "success";
    }
}