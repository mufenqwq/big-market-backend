package site.mufen.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import site.mufen.domain.task.model.entity.TaskEntity;
import site.mufen.domain.task.repository.ITaskRepository;
import site.mufen.infrastructure.event.EventPublisher;
import site.mufen.infrastructure.persistent.dao.ITaskDao;
import site.mufen.infrastructure.persistent.po.Task;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author mufen
 * @Description 任务服务仓储实现
 * @create 2024/11/5 22:34
 */
@Repository
@Slf4j
public class TaskRepository implements ITaskRepository {
    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> taskList = taskDao.queryNoSendMessageTaskList();
        ArrayList<TaskEntity> taskEntities = new ArrayList<>(taskList.size());
        for (Task task : taskList) {
            String userId = task.getUserId();
            String topic = task.getTopic();
            String messageId = task.getMessageId();
            String message = task.getMessage();
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(userId);
            taskEntity.setTopic(topic);
            taskEntity.setMessageId(messageId);
            taskEntity.setMessage(message);
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageCompleted(taskReq);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task taskReq = new Task();
        taskReq.setUserId(userId);
        taskReq.setMessageId(messageId);
        taskDao.updateTaskSendMessageFail(taskReq);
    }
}
