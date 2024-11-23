package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description 未支付的活动订单
 * @create 2024/11/15 14:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidActivityOrderEntity {
    private String userId;
    private String orderId;
    private String outBusinessNo;
    private BigDecimal payAmount;
}
