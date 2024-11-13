package site.mufen.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import site.mufen.domain.credit.model.aggregate.TradeAggregate;
import site.mufen.domain.credit.model.entity.CreditAccountEntity;
import site.mufen.domain.credit.model.entity.CreditOrderEntity;
import site.mufen.domain.credit.model.valobj.TradeNameVO;
import site.mufen.domain.credit.model.valobj.TradeTypeVO;
import site.mufen.domain.credit.repository.ICreditRepository;
import site.mufen.infrastructure.persistent.dao.IUserCreditAccountDao;
import site.mufen.infrastructure.persistent.dao.IUserCreditOrderDao;
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
    private IDBRouterStrategy dbRouter;
    @Resource
    private IRedisService redisService;
    @Resource
    private TransactionTemplate transactionTemplate;

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
                        userCreditAccountDao.updateAddAccount(userCreditAccountReq);
                    }
                    // 2.保存账户订单
                    userCreditOrderDao.insert(userCreditOrderReq);
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
            lock.unlock();
        }
    }
}
