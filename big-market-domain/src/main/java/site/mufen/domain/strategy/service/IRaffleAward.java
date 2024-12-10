package site.mufen.domain.strategy.service;

import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

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

    /**
     * 根据活动id查询奖品列表配置
     * @param activityId 活动Id
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);

    /**
     * 查询有效的活动奖品配置
     * @return 奖品配置列表
     */
    List<StrategyAwardStockKeyVO> queryOpenActivityStrategyAwardList();
}
