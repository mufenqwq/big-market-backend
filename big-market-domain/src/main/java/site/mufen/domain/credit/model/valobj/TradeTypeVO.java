package site.mufen.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:11
 */
@Getter
@AllArgsConstructor
public enum  TradeTypeVO {
    FORWARD("forward", "正向交易，+ 积分"),
    REVERSE("reverse", "反向交易，- 积分"),
    ;
    private final String code;
    private final String desc;
}
