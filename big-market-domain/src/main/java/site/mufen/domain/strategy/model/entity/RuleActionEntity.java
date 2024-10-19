package site.mufen.domain.strategy.model.entity;

import lombok.*;
import site.mufen.domain.strategy.model.vo.RuleLogicCheckTypeVO;

/**
 * @author mufen
 * @Description 规则动作实体
 * @create 2024/10/17 20:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    public static class RaffleEntity {

    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略Id
         */
        private Long strategyId;
        /**
         * 权重值key, 用于抽奖时可以选择权重抽奖
         */
        private String ruleWeightValueKey;
        /**
         * 奖品Id -- 黑名单用户有自己特定的奖品Id
         */
        private Integer awardId;
    }

    public static class RaffleCenterEntity extends RaffleEntity {

    }

    public static class RaffleAfterEntity extends RaffleEntity {

    }


}
