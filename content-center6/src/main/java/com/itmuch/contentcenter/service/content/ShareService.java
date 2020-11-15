package com.itmuch.contentcenter.service.content;

import com.itmuch.contentcenter.dao.content.ShareMapper;
import com.itmuch.contentcenter.domain.dto.content.ShareDTO;
import com.itmuch.contentcenter.domain.dto.user.UserDTO;
import com.itmuch.contentcenter.domain.entity.content.Share;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {
    private final ShareMapper shareMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    /**
     * 自己手写的随机调用
     */
    public ShareDTO findById(Integer id) {
        //获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        //发布人id
        Integer userId = share.getUserId();
        //拿到用户信息所有实例的信息
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        //所有用户中心实例请求的地址
//        List<String> targetUrls = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/users/{id}")
//                .collect(Collectors.toList());
//        //随机来个下标
//        int i = ThreadLocalRandom.current().nextInt(targetUrls.size());
//        log.info("请求的目标地址是:{}", targetUrls.get(i));
//        UserDTO userDTO = restTemplate.getForObject(targetUrls.get(i), UserDTO.class, userId);
        UserDTO userDTO = restTemplate.getForObject("http://user-center/users/{id}", UserDTO.class, userId);
        ShareDTO shareDTO = new ShareDTO();
        //消息的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }
}