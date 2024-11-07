package site.mufen.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description
 * @create 2024/11/5 22:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 主题
     */
    private String topic;
    /**
     * 信息Id
     */
    private String messageId;
    /**
     * 信息主体
     */
    private String message;
}
