package site.mufen.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.RaffleActivityAccountDay;

/**
 * @author mufen
 * @Description 活动账户 - 日参与记录
 * @create 2024/11/4 20:16
 */
@Mapper
public interface IRaffleActivityAccountDayDao {
    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);

    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay raffleActivityAccountDay);

    void insertActivityAccountDay(RaffleActivityAccountDay raffleActivityAccountDay);
}
