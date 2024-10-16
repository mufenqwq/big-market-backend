package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.Strategy;
import site.mufen.infrastructure.persistent.po.StrategyAward;

import java.util.List;

/**
 * @author mufen
 * @Description 抽奖策略奖品的明细配置 dao
 * @create 2024/10/15 15:16
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);
}
