package site.mufen.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.activity.model.entity.ActivityAccountEntity;
import site.mufen.domain.activity.model.entity.ActivityOrderEntity;

/**
 * @author mufen
 * @Description 下单聚合对象
 * @create 2024/11/1 19:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    /**
     * 活动账户实体
     */
    private ActivityAccountEntity activityAccountEntity;
    /**
     * 活动订单对象
     */
    private ActivityOrderEntity activityOrderEntity;
}
