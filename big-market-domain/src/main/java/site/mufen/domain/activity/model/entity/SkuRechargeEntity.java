package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.activity.model.valobj.OrderTradeTypeVO;

/**
 * @author mufen
 * @Description
 * @create 2024/11/2 18:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuRechargeEntity {

    private String userId;
    private Long sku;
    /**
     * 业务仿重ID - 外部透传的，确保幂等
     */
    private String outBusinessNo;
    /**
     * 订单交易类型 - 需不需要支付
     */
    private OrderTradeTypeVO orderTradeTypeVO = OrderTradeTypeVO.rebate_no_pay_trade;
}
