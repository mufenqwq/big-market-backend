package site.mufen.domain.strategy.model.valobj;

import lombok.*;

import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/11/10 22:56
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleWeightVO {
    // 原始规则值配置
    private String ruleValue;
    //权重值
    private Integer weight;
    // 奖品配置
    private List<Integer> awardIds;
    // 奖品集合
    private List<Award> awardList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Award {
        private Integer awardId;
        private String awardTitle;
    }
}
