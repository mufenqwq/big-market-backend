package site.mufen.domain.strategy.service;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author mufen
 * @Description 策略奖品查询接口
 * @create 2024/10/25 14:52
 */
public interface IRaffleAward {


    /**
     *  根据策略ID查询抽奖奖品列表配置
     * @param strategyId 策略Id
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
