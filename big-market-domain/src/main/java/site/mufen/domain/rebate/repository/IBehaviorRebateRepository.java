package site.mufen.domain.rebate.repository;

import site.mufen.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import site.mufen.domain.rebate.model.valobj.BehaviorTypeVO;
import site.mufen.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mufen
 * @Description 行为返利服务仓储接口
 * @create 2024/11/8 22:43
 */
public interface IBehaviorRebateRepository {
    void saveUserRebateRecord(String userId, ArrayList<BehaviorRebateAggregate> behaviorRebateAggregates);

    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

}
