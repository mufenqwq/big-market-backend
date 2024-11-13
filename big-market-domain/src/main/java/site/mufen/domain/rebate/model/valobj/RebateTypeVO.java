package site.mufen.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 返利类型 (sku 活动库存充值商品， integral 用户活动积分)
 * @create 2024/11/9 22:02
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {
    SKU("sku", "活动库存充值商品"),
    INTEGRAL("integral", "用户活动积分"),
    ;
    private final String code;
    private final String info;
}
