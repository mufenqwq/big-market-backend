package site.mufen.domain.strategy.service.armory.algorithm;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/12/10 09:37
 */
public interface IAlgorithm {

    void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange);

    Integer dispatchAlgorithm(String key);
}
