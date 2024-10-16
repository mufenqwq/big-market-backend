package site.mufen.domain.strategy.service.armory;

/**
 * @author mufen
 * @Description 策略装配兵工厂 负责初始化策略计算
 * @create 2024/10/16 23:02
 */
public interface IStrategyArmory {

    boolean assembleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);
}
