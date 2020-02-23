package com.itmuch.contentcenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import com.itmuch.contentcenter.domain.entity.content.Share;
import com.itmuch.contentcenter.feignclient.TestBaiduFeignClient;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.rocketmq.MySource;
import com.itmuch.contentcenter.sentineltest.TestControllerBlockHandlerClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@Slf4j
@RestController
@RefreshScope
public class TestController {
    @Autowired(required = false)
    private ShareMapper shareMapper;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private UserCenterFeignClient userCenterFeignClient;
    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;
    @Autowired
    private TestService testService;

    //    @Autowired
//    private TestUserCenterFeignClient testUserCenterFeignClient;
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
    @GetMapping("test2")
    public List<ServiceInstance> getInstances() {
        // 查询指定服务的所有实例的信息
        // consul/eureka/zookeeper...  即使不适用nacos使用这些也可以调用到
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return this.discoveryClient.getInstances("user-center");
    }

    /**
     * 测试Feign的多参数请求，get请求的方式
     */
    @GetMapping("test-get")
    public UserDTO query(UserDTO userDTO) {
        return userCenterFeignClient.query(userDTO);
    }

    /**
     * 测试Feign脱离Ribbon使用的使用
     *
     * @return
     */
    @GetMapping("baidu")
    public String baiduIndex() {
        return this.testBaiduFeignClient.index();
    }

    @GetMapping("test-a")
    public String testA() {
        this.testService.common();
        return "test-a";
    }

    @GetMapping("test-b")
    public String testB() {
        this.testService.common();
        return "test-b";
    }

    /**
     * 测试热点规则的端点
     *
     * @param a
     * @param b
     * @return
     */
    @GetMapping("test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a, @RequestParam(required = false) String b) {
        return a + " " + b;
    }

    /**
     * 测试使用代码配置Sentinel的规则
     *
     * @return
     */
    @GetMapping("test-add-flow-rule")
    public String testHot() {
        this.initFlowQpsRule();
        return "success";
    }

    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/shares/1");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * 测试Sentinel的API
     */
    @GetMapping("/test-sentinel-api")
    public String testSentinelApi(@RequestParam(required = false) String a) {
        String resourceName = "test-sentinel-api";
        //这里我们可以针对来源
        ContextUtil.enter(resourceName, "test-wfw");
        //定义一个sentinel保护的资源，名称是test-sentinel-api
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            // 被保护的业务逻辑
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        }
        //如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            log.warn("限流，或者降级了", e);
            return "限流，或者降级了";
        } catch (IllegalArgumentException e) {
            // 统计IllegalArgumentException【发生的次数、发生占比...】
            Tracer.trace(e);
            return "参数非法！";
        } finally {
            if (entry != null) {
                // 退出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-resource")
    @SentinelResource(
            value = "test-sentinel-api",
            blockHandler = "block",
            blockHandlerClass = TestControllerBlockHandlerClass.class,
            fallback = "fallback"
    )
    public String testSentinelResource(@RequestParam(required = false) String a) {
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a cannot be blank.");
        }
        return a;
    }
//

    /**
     * 1.5 处理降级
     * - sentinel 1.6 可以处理Throwable
     *
     * @param a
     * @return
     */
    public String fallback(String a) {
        return "限流，或者降级了 fallback";
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test-rest-template-sentinel/{userId}")
    public UserDTO test(@PathVariable Integer userId) {
        return this.restTemplate
                .getForObject(
                        "http://user-center/users/{userId}",
                        UserDTO.class, userId);
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

    @Value("${your.configuration}")
    private String yourConfiguration;
    @GetMapping("/yourConfiguration")
    public String testConfiguration() {
        return yourConfiguration;
    }
//
//    @GetMapping("/tokenRelay/{userId}")
//    public ResponseEntity<UserDTO> tokenRelay(@PathVariable Integer userId, HttpServletRequest request) {
//        String token = request.getHeader("X-Token");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Token", token);
//
//        return this.restTemplate
//                .exchange(
//                        "http://user-center/users/{userId}",
//                        HttpMethod.GET,
//                        new HttpEntity<>(headers),
//                        UserDTO.class,
//                        userId
//                );
//    }
}
