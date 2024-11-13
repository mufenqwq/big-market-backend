package site.mufen.domain.strategy.service;

import site.mufen.domain.strategy.model.valobj.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @author mufen
 * @Description 抽奖规则接口
 * @create 2024/11/7 22:13
 */
public interface IRaffleRule {

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);


    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);

    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);
}
