package site.mufen.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.model.entity.StrategyEntity;
import site.mufen.domain.strategy.model.entity.StrategyRuleEntity;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.domain.strategy.service.armory.algorithm.AbstractAlgorithm;
import site.mufen.domain.strategy.service.armory.algorithm.IAlgorithm;
import site.mufen.types.common.Constants;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author mufen
 * @Description
 * @create 2024/10/16 23:04
 */
@Slf4j
@Service
public class StrategyArmoryDispatch extends AbstractStrategyArmoryDispatch{

    private final Map<String, IAlgorithm> algorithmMap;

    private final Integer ALGORITHM_THRESHOLD_VALUE = 10000;

    public StrategyArmoryDispatch(Map<String, IAlgorithm> algorithmMap) {
        this.algorithmMap = algorithmMap;
    }


    @Override
    protected void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        BigDecimal minAwardRate = minAwardRate(strategyAwardEntities);
        double rateRange = convert(minAwardRate.doubleValue());

        if (rateRange <= ALGORITHM_THRESHOLD_VALUE) {
            IAlgorithm o1Algorithm = algorithmMap.get(AbstractAlgorithm.Algorithm.O1.getKey());
            o1Algorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            // 存储这个key是使用的哪种算法
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.O1.getKey());
        } else {
            IAlgorithm oLogNAlgorithm = algorithmMap.get(AbstractAlgorithm.Algorithm.OLogN.getKey());
            oLogNAlgorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, AbstractAlgorithm.Algorithm.OLogN.getKey());
        }
    }

    @Override
    protected Integer dispatchAlgorithm(String key) {
        String beanName = repository.queryStrategyArmoryAlgorithmFromCache(key);
        IAlgorithm algorithm = algorithmMap.get(beanName);
        return algorithm.dispatchAlgorithm(key);
    }
}
