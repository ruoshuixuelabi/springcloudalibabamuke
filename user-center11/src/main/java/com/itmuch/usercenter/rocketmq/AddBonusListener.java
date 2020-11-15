package com.itmuch.usercenter.rocketmq;

import com.itmuch.usercenter.dao.bonus.BonusEventLogMapper;
import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.domain.entity.bonus.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author admin
 */
@Service
@RocketMQMessageListener(topic = "add-bonus",consumerGroup = "consumerGroup")
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BonusEventLogMapper bonusEventLogMapper;
    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        //当收到消息的时候，执行的业务方法
        Integer userId = message.getUserId();
        Integer bonus = message.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus()+message.getBonus());
        userMapper.updateByPrimaryKey(user);
        //为用户加积分
        bonusEventLogMapper.insert(BonusEventLog
                .builder()
                .userId(userId)
                .value(bonus)
                .event("投稿")
                .createTime(new Date())
                .description("投稿加积分")
                .build());
    }
}
