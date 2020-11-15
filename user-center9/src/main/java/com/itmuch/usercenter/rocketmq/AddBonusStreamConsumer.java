package com.itmuch.usercenter.rocketmq;

import com.itmuch.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AddBonusStreamConsumer {
    private final UserService userService;

     public void receive(UserAddBonusMsgDTO message) {
        message.setEvent("CONTRIBUTE");
        message.setDescription("投稿加积分..");
        this.userService.addBonus(message);
    }
}
