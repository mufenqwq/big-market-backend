package site.mufen.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.activity.model.entity.ActivityAccountDayEntity;
import site.mufen.domain.activity.model.entity.ActivityAccountEntity;
import site.mufen.domain.activity.model.entity.ActivityAccountMonthEntity;
import site.mufen.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @author mufen
 * @Description 参与抽奖活动订单聚合对象
 * @create 2024/11/4 16:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {

    private String userId;
    private Long activityId;
    private ActivityAccountEntity activityAccountEntity;
    private boolean isExistAccountMonth = true;
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    private boolean isExistAccountDay = true;
    private ActivityAccountDayEntity activityAccountDayEntity;
    private UserRaffleOrderEntity userRaffleOrderEntity;
}
