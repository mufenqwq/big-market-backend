package site.mufen.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 日常返利行为配置对象
 * @create 2024/11/8 22:45
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBehaviorRebateVO {
    /**
     * 行为类型(sign-签到，openai_pay 支付)
     */
    private String behaviorType;
    /**
     * 返利描述
     */
    private String rebateDesc;
    /**
     * 返利类型 (sku 返利库存充值商品，integral 用户活动积分)
     */
    private String rebateType;
    /**
     *  返利配置
     */
    private String rebateConfig;
}
