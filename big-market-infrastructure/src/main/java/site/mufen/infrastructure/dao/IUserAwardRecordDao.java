package site.mufen.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.dao.po.UserAwardRecord;

/**
 * @author mufen
 * @Description 用户中奖记录dao
 * @create 2024/11/5 21:31
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDao {

    void insert(UserAwardRecord userAwardRecord);

    int updateUserAwardRecordCompleted(UserAwardRecord userAwardRecordReq);
}
