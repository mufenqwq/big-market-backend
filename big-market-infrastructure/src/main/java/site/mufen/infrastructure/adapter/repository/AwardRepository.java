package site.mufen.infrastructure.adapter.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import site.mufen.domain.award.model.aggregate.GiveOutPrizesAggregate;
import site.mufen.domain.award.model.aggregate.UserAwardRecordAggregate;
import site.mufen.domain.award.model.entity.TaskEntity;
import site.mufen.domain.award.model.entity.UserAwardRecordEntity;
import site.mufen.domain.award.model.entity.UserCreditAwardEntity;
import site.mufen.domain.award.model.valobj.AccountStatusVO;
import site.mufen.domain.award.model.valobj.AwardStateVO;
import site.mufen.domain.award.repository.IAwardRepository;
import site.mufen.infrastructure.dao.*;
import site.mufen.infrastructure.event.EventPublisher;
import site.mufen.infrastructure.dao.po.Task;
import site.mufen.infrastructure.dao.po.UserAwardRecord;
import site.mufen.infrastructure.dao.po.UserCreditAccount;
import site.mufen.infrastructure.dao.po.UserRaffleOrder;
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
    private IAwardDao awardDao;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
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

    /**
     * 根据awardId查询awardConfig
     *
     * @param awardId 奖品Id
     * @return awardConfig
     */
    @Override
    public String queryAwardConfig(Integer awardId) {
        return awardDao.queryAwardConfigByAwardId(awardId);
    }

    /**
     * 存储发奖对象
     *
     * @param giveOutPrizesAggregate 发奖聚合对象
     */
    @Override
    public void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();


        // 更新订单状态
        UserAwardRecord userAwardRecordReq = new UserAwardRecord();
        userAwardRecordReq.setUserId(userId);
        userAwardRecordReq.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecordReq.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        //更新用户积分 首次插入
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.open.getCode());


        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 更新奖品记录
                    int updateAwardCount = userAwardRecordDao.updateUserAwardRecordCompleted(userAwardRecordReq);
                    if (0 == updateAwardCount) {
                        log.warn("更新中奖记录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        status.setRollbackOnly();
                    }
                    int updateAccountCount = userCreditAccountDao.updateAddAccount(userCreditAccountReq);
                    if (0 == updateAccountCount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("更新中奖记录，唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
                }
            });
        } finally {
            dbRouter.clear();
        }

    }

    @Override
    public String queryAwardKey(Integer awardId) {
        return awardDao.queryAwardKeyByAwardId(awardId);
    }
}
