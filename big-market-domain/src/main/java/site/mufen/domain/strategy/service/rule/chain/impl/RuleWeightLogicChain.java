package site.mufen.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.model.entity.RuleActionEntity;
import site.mufen.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.domain.strategy.service.armory.IStrategyDispatch;
import site.mufen.domain.strategy.service.rule.chain.AbstractLogicChain;
import site.mufen.types.common.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mufen
 * @Description 权重
 * @create 2024/10/21 11:18
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Resource
    protected IStrategyDispatch strategyDispatch;

    public Long userScore = 0L;

    /**
     * 权重责任链过滤 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * 解析数据格式 判断哪个范围符合用户的特定抽奖范围
     * @param userId 用户Id
     * @param strategyId 策略Id
     * @return 奖品Id
     */
    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId {} strategyId {} ruleModel {}", userId, strategyId, ruleModel());

        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());

        // 1. 根据用户id查询用户抽奖消耗的积分值，先写成固定的值
        // todo 后续需要从数据库中查询
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) return null;

        // 2.转换key值 并默认排序
        ArrayList<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Long nextValue = analyticalSortedKeys.stream().sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeysValue -> userScore >= analyticalSortedKeysValue)
                .findFirst()
                .orElse(null);
        if (null != nextValue) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId {} strategyId {} ruleModel {} awardId {}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }
        // 3.  放行 过滤其他责任链
        log.info("抽奖责任链-权重放行 userId {} strategyId {} ruleModel {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        HashMap<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (null == ruleValueKey || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
