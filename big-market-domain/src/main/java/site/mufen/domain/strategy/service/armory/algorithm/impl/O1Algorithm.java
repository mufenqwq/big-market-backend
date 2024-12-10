package site.mufen.domain.strategy.service.armory.algorithm.impl;

import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.service.armory.algorithm.AbstractAlgorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author mufen
 * @Description
 * @create 2024/12/10 14:29
 */
@Component("o1Algorithm")
public class O1Algorithm extends AbstractAlgorithm {
    @Override
    public void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange) {

        // 1. 生成概率奖品查找表
        ArrayList<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            // 5. 计算每个概率需要存放到查找表的数量
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 6. 乱序
        Collections.shuffle(strategyAwardSearchRateTables);


        // 7. 转换成集合
        HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 8. 存储到redis
        repository.storeStrategyAwardSearchRateTables(key, shuffleStrategyAwardSearchRateTables.size(), shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public Integer dispatchAlgorithm(String key) {
        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(Long.valueOf(key), new SecureRandom().nextInt(rateRange));
    }
}
