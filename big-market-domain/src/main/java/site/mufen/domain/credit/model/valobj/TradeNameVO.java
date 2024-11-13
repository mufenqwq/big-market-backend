package site.mufen.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:13
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {
    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),
    ;
    private final String name;
}
