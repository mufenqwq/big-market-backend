package site.mufen.infrastructure.persistent.dao;


import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.UserBehaviorRebateOrder;

import java.util.List;

/**
 * @author mufen
 * @Description 用户行为返利订单表Dao
 * @create 2024/11/8 21:03
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {

    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

    @DBRouter
    List<UserBehaviorRebateOrder> queryOrderByOutBusinessNo(UserBehaviorRebateOrder userBehaviorRebateOrder);

}
