package site.mufen.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import site.mufen.domain.credit.model.aggregate.TradeAggregate;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.CreditOrderEntity;
import site.mufen.domain.credit.model.entity.TaskEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;
import site.mufen.domain.credit.repository.ICreditRepository;
import site.mufen.domain.rebate.model.valobj.TaskStateVO;
import site.mufen.infrastructure.event.EventPublisher;
import site.mufen.infrastructure.persistent.dao.ITaskDao;
import site.mufen.infrastructure.persistent.dao.IUserCreditAccountDao;
import site.mufen.infrastructure.persistent.dao.IUserCreditOrderDao;
import site.mufen.infrastructure.persistent.po.Task;
import site.mufen.infrastructure.persistent.po.UserCreditAccount;
import site.mufen.infrastructure.persistent.po.UserCreditOrder;
import site.mufen.infrastructure.persistent.redis.IRedisService;
import site.mufen.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author mufen
 * @Description
 * @create 2024/11/14 00:01
 */
@Slf4j
@Repository
public class CreditRepository implements ICreditRepository {
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserCreditOrderDao userCreditOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private IRedisService redisService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();

        // 积分账户
        BigDecimal adjustAmount = creditAccountEntity.getAdjustAmount();

        // 订单
        String orderId = creditOrderEntity.getOrderId();
        TradeNameVO tradeNameVO = creditOrderEntity.getTradeNameVO();
        TradeTypeVO tradeTypeVO = creditOrderEntity.getTradeTypeVO();
        BigDecimal tradeAmount = creditOrderEntity.getTradeAmount();
        String outBusinessNo = creditOrderEntity.getOutBusinessNo();

        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(adjustAmount);
        userCreditAccountReq.setAvailableAmount(adjustAmount);

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(userId);
        userCreditOrderReq.setOrderId(orderId);
        userCreditOrderReq.setTradeName(tradeNameVO.getName());
        userCreditOrderReq.setTradeType(tradeTypeVO.getCode());
        userCreditOrderReq.setTradeAmount(tradeAmount);
        userCreditOrderReq.setOutBusinessNo(outBusinessNo);

        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        Task task = new Task();
        task.setUserId(userId);
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(TaskStateVO.create.getCode());


        RLock lock = redisService.getLock(Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + outBusinessNo);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 1.保存积分账户
                    UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (null == userCreditAccount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    }else {
                        BigDecimal availableAmount = userCreditAccountReq.getAvailableAmount();
                        if (availableAmount.compareTo(BigDecimal.ZERO) >= 0) {
                            userCreditAccountDao.updateAddAccount(userCreditAccountReq);
                        } else {
                            userCreditAccountDao.updateSubtractionAccount(userCreditAccountReq);
                        }
                    }
                    // 2.保存账户订单
                    userCreditOrderDao.insert(userCreditOrderReq);
                    // 3.写入任务
                    taskDao.insert(task);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
            });

        } finally {
            dbRouter.clear();
            if (lock.isLocked()) lock.unlock();
        }

        // 在事务外面发送MQ消息
        try {
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
        } catch (Exception e) {
            log.error("调整账户积分记录，发送MQ消息失败 userId:{} topic:{}", task.getUserId(), task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }

    @Override
    public CreditAccountEntity queryUserCreditAccount(String userId) {
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        try {
            dbRouter.doRouter(userId);
            UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
            return CreditAccountEntity.builder().userId(userId).adjustAmount(userCreditAccount.getAvailableAmount()).build();
        } finally {
            dbRouter.clear();
        }
    }
}
