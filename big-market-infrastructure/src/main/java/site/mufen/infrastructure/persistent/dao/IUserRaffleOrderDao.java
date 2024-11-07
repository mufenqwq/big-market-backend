package site.mufen.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.domain.activity.model.entity.UserRaffleOrderEntity;
import site.mufen.infrastructure.persistent.po.UserRaffleOrder;

/**
 * @author mufen
 * @Description 抽奖订单Dao
 * @create 2024/11/4 20:29
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDao {

    @DBRouter
    UserRaffleOrder queryNoUseRaffleOrder(UserRaffleOrderEntity userRaffleOrderReq);

    void insert(UserRaffleOrder userRaffleOrder);

    int updateUserRaffleOrderStateUsed(UserRaffleOrder userRaffleOrderReq);
}
