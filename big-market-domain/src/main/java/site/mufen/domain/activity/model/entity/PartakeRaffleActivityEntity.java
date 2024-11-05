package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 参与抽奖的实体对象
 * @create 2024/11/4 15:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartakeRaffleActivityEntity {
    private String userId;
    private Long activityId;
}
