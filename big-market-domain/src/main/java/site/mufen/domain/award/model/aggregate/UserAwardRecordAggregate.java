package site.mufen.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.award.model.entity.TaskEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @author mufen
 * @Description  用户中奖记录聚合对象 【一个聚合就是一个聚合对象】
 * @create 2024/11/5 17:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {
    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
