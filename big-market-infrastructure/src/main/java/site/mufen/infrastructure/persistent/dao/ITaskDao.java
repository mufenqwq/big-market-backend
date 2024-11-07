package site.mufen.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * @author mufen
 * @Description task任务表dao
 * @create 2024/11/5 21:26
 */
@Mapper
public interface ITaskDao {
    void insert(Task task);

    @DBRouter
    void updateTaskSendMessageCompleted(Task task);

    List<Task> queryNoSendMessageTaskList();

    @DBRouter
    void updateTaskSendMessageFail(Task taskReq);
}
