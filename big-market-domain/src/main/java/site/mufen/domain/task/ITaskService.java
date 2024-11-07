package site.mufen.domain.task;

import site.mufen.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author mufen
 * @Description 消息任务接口
 * @create 2024/11/5 22:16
 */
public interface ITaskService {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);
    void updateTaskSendMessageFail(String userId, String messageId);
}
