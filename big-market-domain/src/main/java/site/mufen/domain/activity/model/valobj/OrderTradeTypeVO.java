package site.mufen.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 订单交易类型实体
 * @create 2024/11/14 21:18
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {
    credit_pay_trade("credit_pay_trade", "积分兑换，需要支付类交易"),
    rebate_no_pay_trade("rebate_no_pay_trade", "返利类型，不需要支付类交易"),
    ;
    private final String code;
    private final String desc;
}
