package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mufen
 * @Description 抽奖奖品列表
 * @create 2024/10/25 00:58
 */
@Data
public class RaffleAwardListRequestDTO implements Serializable {


    // 用户id
    private String userId;

    // 活动Id
    private Long activityId;
}
