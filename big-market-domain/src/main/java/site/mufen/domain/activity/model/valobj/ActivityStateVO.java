package site.mufen.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 订单状态枚举值对象
 * @create 2024/11/1 18:46
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {
    create("create", "创建"),
    open("open", "开启"),
    close("close", "关闭"),
    ;
    private final String code;
    private final String desc;

}
