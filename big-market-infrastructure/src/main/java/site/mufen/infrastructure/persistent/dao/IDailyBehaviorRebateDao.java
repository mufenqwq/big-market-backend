package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import site.mufen.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @author mufen
 * @Description 日常行为返利活动配置Dao
 * @create 2024/11/8 21:02
 */
@Mapper
public interface IDailyBehaviorRebateDao {
    List<DailyBehaviorRebate> queryDailyBehaviorRebateConfig(String code);
}
