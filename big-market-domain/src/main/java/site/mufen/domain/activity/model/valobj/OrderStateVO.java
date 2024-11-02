package site.mufen.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 活动状态值对象
 * @create 2024/11/1 18:47
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {
    completed("completed", "完成"),

    ;
    private final String code;
    private final String desc;
}
