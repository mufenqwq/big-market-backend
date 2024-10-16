package site.mufen.domain.strategy.repository;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/10/16 23:05
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(Long strategyId, BigDecimal rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
