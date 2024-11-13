package site.mufen.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author mufen
 * @Description 用户状态枚举
 * @create 2024/11/13 15:42
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {
    open("open", "开启"),
    close("close", "冻结"),
    ;
    private final String code;
    private final String desc;
}
