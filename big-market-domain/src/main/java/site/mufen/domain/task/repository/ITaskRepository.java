package site.mufen.domain.task.repository;

import site.mufen.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author mufen
 * @Description 人物服务仓储接口
 * @create 2024/11/5 22:19
 */
public interface ITaskRepository {
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
