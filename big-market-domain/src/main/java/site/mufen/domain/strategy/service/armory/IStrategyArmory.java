package site.mufen.domain.strategy.service.armory;

/**
 * @author mufen
 * @Description 策略装配兵工厂 负责初始化策略计算
 * @create 2024/10/16 23:02
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置 (触发的时机可以为活动审核通过后进行调用)
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
