package site.mufen.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.RaffleActivityAccount;

/**
 * @author mufen
 * @Description 抽奖活动账户表Dao
 * @create 2024/10/31 20:52
 */
@Mapper
public interface IRaffleActivityAccountDao {
    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);

    int updateActivityAccountSubtractionQuota(RaffleActivityAccount build);

    void updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount build);

    void updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount build);

    @DBRouter
    RaffleActivityAccount queryActivityAccountByUserId(RaffleActivityAccount raffleActivityAccountReq);
}
