package site.mufen.domain.rebate.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import site.mufen.domain.rebate.model.entity.TaskEntity;

/**
 * @author mufen
 * @Description 行为返利聚合对象
 * @create 2024/11/8 22:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    // 用户Id
    private String userId;
    // 行为返利订单实体
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    // 任务实体
    private TaskEntity taskEntity;

}
