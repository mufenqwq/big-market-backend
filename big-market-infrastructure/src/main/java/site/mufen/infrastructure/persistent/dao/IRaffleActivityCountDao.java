package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.RaffleActivityCount;

/**
 * @author mufen
 * @Description 抽奖活动次数配置表Dao
 * @create 2024/10/31 20:53
 */
@Mapper
public interface IRaffleActivityCountDao {

    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
