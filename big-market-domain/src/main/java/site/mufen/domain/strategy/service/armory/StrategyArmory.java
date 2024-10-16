package site.mufen.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;
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
 * @create 2024/10/16 23:04
 */
@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory{

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities =  repository.queryStrategyAwardList(strategyId);
        // 判断一下是否有这个策略配置
        if (null == strategyAwardEntities || strategyAwardEntities.isEmpty()) return false;

        // 2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream().map(StrategyAwardEntity::getAwardRate).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);



        // 3. 获取概率值的总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 获取概率范围
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

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
        repository.storeStrategyAwardSearchRateTables(strategyId, rateRange, shuffleStrategyAwardSearchRateTables);
        return true;

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }


}
