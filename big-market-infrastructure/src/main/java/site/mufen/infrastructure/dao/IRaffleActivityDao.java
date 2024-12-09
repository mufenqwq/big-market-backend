package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.dao.po.RaffleActivity;

/**
 * @author mufen
 * @Description 抽奖活动表Dao
 * @create 2024/10/31 20:50
 */
@Mapper
public interface IRaffleActivityDao {
    
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    Long queryStrategyIdByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);
}
