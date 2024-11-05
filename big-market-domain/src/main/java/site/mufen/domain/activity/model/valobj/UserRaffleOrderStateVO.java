package site.mufen.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 用户抽奖订单状态对象
 * @create 2024/11/4 15:27
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {
    create("create", "创建"),
    used("used", "已使用"),
    canceled("cancel", "失效"),
    ;
    private final String code;
    private final String desc;
}
