package site.mufen.infrastructure.persistent.repository;

import org.redisson.api.RMap;
import org.springframework.stereotype.Repository;
import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.infrastructure.persistent.dao.IStrategyAwardDao;
import site.mufen.infrastructure.persistent.po.StrategyAward;
import site.mufen.infrastructure.persistent.redis.IRedisService;
import site.mufen.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author mufen
 * @Description 策略仓储实现
 * @create 2024/10/16 23:06
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String cachedKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cachedKey);
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) return strategyAwardEntities;

        // 从库中获取数据
        List<StrategyAward> strategyAwards =  strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities =  new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                        .strategyId(strategyAward.getStrategyId())
                        .awardId(strategyAward.getAwardId())
                        .awardCount(strategyAward.getAwardCount())
                        .awardCountSurplus(strategyAward.getAwardCountSurplus())
                        .awardRate(strategyAward.getAwardRate())
                        .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redisService.setValue(cachedKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRateTables(Long strategyId, BigDecimal rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables) {
        // 1.存储抽奖策略范围值 如10000 用于生成10000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange.intValue());

        // 2. 存储概率查找表
        RMap<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(shuffleStrategyAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }

}
