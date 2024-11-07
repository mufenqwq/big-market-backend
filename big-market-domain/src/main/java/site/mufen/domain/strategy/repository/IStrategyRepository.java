package site.mufen.domain.strategy.repository;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.model.entity.StrategyEntity;
import site.mufen.domain.strategy.model.entity.StrategyRuleEntity;
import site.mufen.domain.strategy.model.valobj.RuleTreeVO;
import site.mufen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import site.mufen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mufen
 * @Description
 * @create 2024/10/16 23:05
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(String key, BigDecimal rateRange, HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**
     *
     * @param cacheKey 保存在redis的key
     * @param awardCount 库存量
     */
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    /**
     * 缓存 key decr 方式扣减库存
     * @param cacheKey 缓存key
     * @return 扣减结果
     */
    Boolean subtractionAwardStock(String cacheKey);

    /**
     * 写入奖品库存消耗队列
     * @param strategyAwardStockKeyVO 奖品库存值对象
     */
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    /**
     * 获取库存奖品消耗队列
     * @return 库存奖品值对象
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新数据库中的库存
     * @param strategyId 策略Id
     * @param awardId 奖品Id
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    /**
     * 查询策略奖品实体
     * @param strategyId 策略Id
     * @param awardId 奖品Id
     * @return 策略奖品实体
     */
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserRaffleCount(String userId, Long strategyId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
