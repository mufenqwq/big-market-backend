package site.mufen.domain.task.service;

import org.springframework.stereotype.Service;
import site.mufen.domain.task.model.entity.TaskEntity;
import site.mufen.domain.task.ITaskService;
import site.mufen.domain.task.repository.ITaskRepository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author mufen
 * @Description 任务接口
 * @create 2024/11/5 22:25
 */
@Service
public class TaskService implements ITaskService {

    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFail(userId, messageId);
    }
}
