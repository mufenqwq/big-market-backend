package site.mufen.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mufen
 * @Description 任务实体
 * @create 2024/11/8 22:54
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {
    create("create", "创建"),
    complete("complete", "发送完成"),
    fail("fail", "发送失败"),
    ;
    private final String code;
    private final String desc;
}
