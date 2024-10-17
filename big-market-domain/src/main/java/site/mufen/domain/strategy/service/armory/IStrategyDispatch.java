package site.mufen.domain.strategy.service.armory;

/**
 * @author mufen
 * @Description 策略抽奖的调度
 * @create 2024/10/17 09:13
 */
public interface IStrategyDispatch {

    /**
     * 获取抽奖策略装配的随机结果
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

}