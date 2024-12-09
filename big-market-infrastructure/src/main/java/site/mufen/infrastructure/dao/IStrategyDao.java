package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.dao.po.Strategy;

import java.util.List;


/**
 * @author mufen
 * @Description 抽奖策略 dao
 * @create 2024/10/15 15:15
 */
@Mapper
public interface IStrategyDao {
    List<Strategy>  queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);
}
