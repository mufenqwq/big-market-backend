package site.mufen.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.dao.po.UserCreditOrder;

/**
 * @author mufen
 * @Description 用户积分订单Dao
 * @create 2024/11/13 23:02
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao {
    void insert(UserCreditOrder userCreditOrder);
}
