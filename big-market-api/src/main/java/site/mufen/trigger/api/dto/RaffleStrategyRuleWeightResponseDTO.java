package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 22:40
 */
@Data
public class RaffleStrategyRuleWeightResponseDTO implements Serializable {
    // 奖品权重值
    private Integer ruleWeightCount;
    // 用户活动账户总使用次数
    private Integer userActivityAccountTotalUseCount;
    // 奖品列表
    private List<StrategyAward> strategyAwards;

    @Data
    public static class StrategyAward {
        private Integer awardId;
        private String awardTitle;
    }
}
