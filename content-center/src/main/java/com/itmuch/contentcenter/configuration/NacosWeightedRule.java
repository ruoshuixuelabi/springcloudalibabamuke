package com.itmuch.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 扩展ribbon的负载均衡器，让他支持nacos的权重
 * 我们可以实现IRule接口,自然也可以通过继承抽象类AbstractLoadBalancerRule来实现扩展
 *
 * @author admin
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 读取配置文件，并初始化NacosWeightedRule，其实大部分的Ribbon内部这个方法也是空的
     *
     * @param clientConfig
     */
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

    /**
     * 实现扩展的时候主要是这个方法
     *
     * @param key
     * @return
     */
    @Override
    public Server choose(Object key) {
        try {
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            log.info("lb = {}", loadBalancer);
            //想要请求的微服务的名称
            String name = loadBalancer.getName();
            //拿到服务发现的相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //Nacos Client自动通过基于权重的负载均衡算法，给我们选择一个实例。
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("选择的实例是：port = {}, instance = {}", instance.getPort(), instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            return null;
        }
    }
}
// spring cloud commons --> 定义了标准
// spring cloud loadbalancer --> 没有权重
