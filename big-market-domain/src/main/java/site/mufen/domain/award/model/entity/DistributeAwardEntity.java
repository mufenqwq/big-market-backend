package site.mufen.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 分发奖品实体对象
 * @create 2024/11/13 12:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributeAwardEntity {

    private String userId;
    private String orderId;
    private Integer awardId;
    private String awardConfig;
}
