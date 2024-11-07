package site.mufen.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.award.event.SendAwardMessageEvent;
import site.mufen.domain.award.model.valobj.TaskStateVO;
import site.mufen.types.event.BaseEvent;

/**
 * @author mufen
 * @Description 任务实体对象
 * @create 2024/11/5 17:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息Id
     */
    private String messageId;
    /**
     * 消息主体
     */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /**
     * 人物状态 create-创建 completed-完成 fail-失败
     */
    private TaskStateVO state;
}
