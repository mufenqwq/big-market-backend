package site.mufen.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.rebate.model.valobj.BehaviorTypeVO;

/**
 * @author mufen
 * @Description 行为实体
 * @create 2024/11/8 22:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 行为类型 sign-签到 openai-pay 支付
     */
    private BehaviorTypeVO behaviorTypeVO;
    /**
     * 业务唯一Id - 实现幂等
     */
    private String outBusinessNo;
}
