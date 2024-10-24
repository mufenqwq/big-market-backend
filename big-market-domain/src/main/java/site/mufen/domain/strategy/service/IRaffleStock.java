package site.mufen.domain.strategy.service;

import site.mufen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author mufen
 * @Description 抽奖库存相关服务，获取库存消耗队列
 * @create 2024/10/24 01:11
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
