package site.mufen.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 行为类型枚举值
 * @create 2024/11/8 22:27
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {
    SIGN("sign", "签到"),
    OPENAI_PAY("openai_pay", "openai 外部支付完成"),
    ;
    private String code;
    private String desc;
}
