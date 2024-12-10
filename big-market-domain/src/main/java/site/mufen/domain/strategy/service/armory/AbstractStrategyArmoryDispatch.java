package site.mufen.domain.strategy.service.armory;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.model.entity.StrategyEntity;
import site.mufen.domain.strategy.model.entity.StrategyRuleEntity;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.types.common.Constants;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author mufen
 * @Description
 * @create 2024/12/10 16:06
 */
public abstract class AbstractStrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();



    // todo 抽象方法让子类去做实现
    protected abstract void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities);

    protected abstract Integer dispatchAlgorithm(String key);




    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities =  repository.queryStrategyAwardList(strategyId);
        // 判断一下是否有这个策略配置
        if (null == strategyAwardEntities || strategyAwardEntities.isEmpty()) return false;

        // 2. 缓存奖品库存 用于 decr扣减库存使用
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        // 3.1 默认装配配置 （全量抽奖概率
        armoryAlgorithm(String.valueOf(strategyId), strategyAwardEntities);



        // 3.2 权重策略配置 - 适用于 rule_weight 权重规则配置
        StrategyEntity strategyEntity =  repository.queryStrategyEntityByStrategyId(strategyId);
        // todo 判断ruleWeight 是否为空
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) return true;

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            armoryAlgorithm(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }

        return true;

    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        return dispatchAlgorithm(String.valueOf(strategyId));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        return dispatchAlgorithm(key);
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDateTime) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.subtractionAwardStock(cacheKey, endDateTime);
    }


    /**
     * 缓存奖品库存到Redis
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @param awardCount 奖品库存
     */
    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }


    // 概率最小值
    protected BigDecimal minAwardRate(List<StrategyAwardEntity> strategyAwardEntities){
        return strategyAwardEntities.stream()
            .map(StrategyAwardEntity::getAwardRate)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }

    // 概率范围值，百分位、千分位、万分位
    protected double convert(double min) {
        if (0 == min) return 1D;

        String minStr = String.valueOf(min);

        // 小数点前
        String beginVale = minStr.substring(0, minStr.indexOf("."));
        int beginLength = 0;
        if (Double.parseDouble(beginVale) > 0) {
            beginLength = minStr.substring(0, minStr.indexOf(".")).length();
        }

        // 小数点后
        String endValue = minStr.substring(minStr.indexOf(".") + 1);
        int endLength = 0;
        if (Double.parseDouble(endValue) > 0) {
            endLength = minStr.substring(minStr.indexOf(".") + 1).length();
        }

        return Math.pow(10, beginLength + endLength);
    }
}
