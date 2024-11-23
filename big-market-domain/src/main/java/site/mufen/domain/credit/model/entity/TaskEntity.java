package site.mufen.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.credit.event.CreditAdjustSuccessMessageEvent;
import site.mufen.domain.rebate.model.valobj.TaskStateVO;
import site.mufen.types.event.BaseEvent;

/**
 * @author mufen
 * @Description 任务对象
 * @create 2024/11/14 23:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    private String userId;
    private String topic;
    private String messageId;
    private BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message;
    /**
     * 任务状态:create-创建 completed-完成 fail-失败
     */
    private TaskStateVO state;
}
