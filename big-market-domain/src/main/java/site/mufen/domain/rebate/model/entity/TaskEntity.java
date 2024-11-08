package site.mufen.domain.rebate.model.entity;

import lombok.*;
import site.mufen.domain.rebate.event.SendRebateMessageEvent;
import site.mufen.domain.rebate.model.valobj.TaskStateVO;
import site.mufen.types.event.BaseEvent;

/**
 * @author mufen
 * @Description 任务实体
 * @create 2024/11/8 22:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    private String userId;

    private String topic;
    private String messageId;
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    private TaskStateVO state;
}
