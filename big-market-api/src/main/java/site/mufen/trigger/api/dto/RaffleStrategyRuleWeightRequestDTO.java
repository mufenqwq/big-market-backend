package site.mufen.trigger.api.dto;

import lombok.Data;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 22:39
 */
@Data
public class RaffleStrategyRuleWeightRequestDTO {
    // 用户Id
    private String userId;
    // 活动Id
    private Long activityId;
}
