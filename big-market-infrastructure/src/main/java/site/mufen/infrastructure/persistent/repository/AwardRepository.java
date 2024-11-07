package site.mufen.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import site.mufen.domain.award.model.aggregate.UserAwardRecordAggregate;
import site.mufen.domain.award.model.entity.TaskEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.valobj.AwardStateVO;
import site.mufen.domain.award.repository.IAwardRepository;
import site.mufen.infrastructure.event.EventPublisher;
import site.mufen.infrastructure.persistent.dao.ITaskDao;
import site.mufen.infrastructure.persistent.dao.IUserAwardRecordDao;
import site.mufen.infrastructure.persistent.dao.IUserRaffleOrderDao;
import site.mufen.infrastructure.persistent.po.Task;
import site.mufen.infrastructure.persistent.po.UserAwardRecord;
import site.mufen.infrastructure.persistent.po.UserRaffleOrder;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author mufen
 * @Description 奖品仓储服务
 * @create 2024/11/5 19:21
 */
@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Long strategyId = userAwardRecordEntity.getStrategyId();
        String orderId = userAwardRecordEntity.getOrderId();
        Integer awardId = userAwardRecordEntity.getAwardId();
        String awardTitle = userAwardRecordEntity.getAwardTitle();
        Date awardTime = userAwardRecordEntity.getAwardTime();
        AwardStateVO awardState = userAwardRecordEntity.getAwardState();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setActivityId(activityId);
        userAwardRecord.setStrategyId(strategyId);
        userAwardRecord.setOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardTitle(awardTitle);
        userAwardRecord.setAwardTime(awardTime);
        userAwardRecord.setAwardState(awardState.getCode());

        Task task = new Task();
        task.setUserId(userId);
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(userAwardRecord.getUserId());
        userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());

        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    // 写入任务
                    taskDao.insert(task);
                    // 更新抽奖单
                    int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrderReq);
                    if (1 != count) {
                        status.setRollbackOnly();
                        log.error("写入中奖任务，用户抽奖单已使用，不可重复使用 userId:{}, activityId:{}, awardId:{}", userId, activityId, awardId);
                        throw new AppException(ResponseCode.ACTIVITY_RAFFLE_ORDER_ERROR.getCode(), ResponseCode.ACTIVITY_RAFFLE_ORDER_ERROR.getInfo());
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖任务，唯一索引冲突 userId:{}, activityId:{}, awardId:{}", userId, activityId, awardId);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
                }
            });
        } finally {
            dbRouter.clear();
        }

        try {
            // 发送消息
            eventPublisher.publish(taskEntity.getTopic(), task.getMessage());
            // 更新数据库记录 task记录表
            log.info("更新task表 任务消息发送完成");
            taskDao.updateTaskSendMessageCompleted(task);
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败: userId:{}, topic:{}", userId, task.getTopic());
        }

    }
}
