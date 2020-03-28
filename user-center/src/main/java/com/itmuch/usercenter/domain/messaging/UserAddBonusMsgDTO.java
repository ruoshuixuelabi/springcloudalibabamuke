package com.itmuch.usercenter.domain.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddBonusMsgDTO {
    /**
     * 为谁加积分
     */
    private Integer userId;
    /**
     * 加多少积分
     */
    private Integer bonus;
}