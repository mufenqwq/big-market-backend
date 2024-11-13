package site.mufen.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import site.mufen.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import site.mufen.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import site.mufen.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import site.mufen.domain.activity.model.entity.*;
import site.mufen.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import site.mufen.domain.activity.model.valobj.ActivityStateVO;
import site.mufen.domain.activity.model.valobj.UserRaffleOrderStateVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.infrastructure.event.EventPublisher;
import site.mufen.infrastructure.persistent.dao.*;
import site.mufen.infrastructure.persistent.po.*;
import site.mufen.infrastructure.persistent.redis.IRedisService;
import site.mufen.types.common.Constants;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mufen
 * @Description 活动仓库实现
 * @create 2024/11/1 20:33
 */
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountMonthDao raffleActivityAccountMonthDao;
    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;


    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryRaffleActivitySku(sku);
        ActivitySkuEntity build = ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
        return build;
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        // 优先从缓存中获取
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;
        // 从库中获取数据
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        ActivityEntity activityEntityBuild = ActivityEntity.builder()
                .activityId(activityId)
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey, activityEntityBuild);
        return activityEntityBuild;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) return activityCountEntity;
        // 从库中获取数据
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        try {
            // 订单对象
            ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();

            raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
            raffleActivityOrder.setSku(activityOrderEntity.getSku());
            raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
            raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
            raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            // 账户对象 - 总
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());

            // 账户对象 - 月
            RaffleActivityAccountMonth raffleActivityAccountMonth = new RaffleActivityAccountMonth();
            raffleActivityAccountMonth.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountMonth.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountMonth.setMonth(raffleActivityAccountMonth.currentMonth());
            raffleActivityAccountMonth.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccountMonth.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());

            // 账户对象 -日
            RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
            raffleActivityAccountDay.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccountDay.setActivityId(createQuotaOrderAggregate.getActivityId());
            raffleActivityAccountDay.setDay(raffleActivityAccountDay.currentDay());
            raffleActivityAccountDay.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccountDay.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());


            // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
            dbRouter.doRouter(createQuotaOrderAggregate.getUserId());
            // 编程式事务
            transactionTemplate.execute(status -> {
                try {
                    // 1. 写入订单
                    raffleActivityOrderDao.insert(raffleActivityOrder);
                    // 2. 更新账户
                    int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                    // 3. 创建账户 - 更新为0，则账户不存在，创新新账户。
                    if (0 == count) {
                        raffleActivityAccountDao.insert(raffleActivityAccount);
                    }
                    // 4. 更新月账户
                    raffleActivityAccountMonthDao.addAccountQuota(raffleActivityAccountMonth);
                    //5. 更新日账户
                    raffleActivityAccountDayDao.addAccountQuota(raffleActivityAccountDay);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus == 0) {
            //todo 发送mq消息
            eventPublisher.publish(activitySkuStockZeroMessageEvent.topic(), activitySkuStockZeroMessageEvent.buildEventMessage(sku));
            return false;
        } else if (surplus < 0) {
            // 库存小于0 恢复为0
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }

        // 1. 按照cacheKey decr 后的值，如 99、98、97 和 key 组成为库存锁的key进行使用。
        // 2. 加锁为了兜底，如果后续有恢复库存，手动处理等【运营是人来操作，会有这种情况发放，系统要做防护】，也不会超卖。因为所有的可用库存key，都被加锁了。
        // 3. 设置加锁时间为活动到期 + 延迟1天
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("活动sku库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<Object> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<Object> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUEUE_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        destinationQueue.clear();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        raffleActivitySkuDao.updateActivitySKuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        raffleActivitySkuDao.clearActivitySkuStock(sku);
    }

    @Override
    public void saveCreatePartakeAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        try {
            String userId = createPartakeOrderAggregate.getUserId();
            Long activityId = createPartakeOrderAggregate.getActivityId();
            ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
            ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
            ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
            UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();

            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 1.更新总账户
                    int totalCount = raffleActivityAccountDao.updateActivityAccountSubtractionQuota(
                            RaffleActivityAccount.builder()
                                    .userId(userId)
                                    .activityId(activityId)
                                    .build()
                    );
                    if (1 != totalCount) {
                        status.setRollbackOnly();
                        log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId:{} activityId:{}", userId, activityId);
                        throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
                    }

                    // 2. 创建或更新月账户
                    if (createPartakeOrderAggregate.isExistAccountMonth()) {
                        int updateMonthCount = raffleActivityAccountMonthDao.updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountMonth.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .month(activityAccountMonthEntity.getMonth())
                                .build());
                        if (1 != updateMonthCount) {
                            // 未更新成功则回滚
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {} month: {}", userId, activityId, activityAccountMonthEntity.getMonth());
                            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
                        }
                    } else {
                        raffleActivityAccountMonthDao.insertActivityAccountMonth(RaffleActivityAccountMonth.builder()
                                        .userId(activityAccountMonthEntity.getUserId())
                                        .activityId(activityAccountMonthEntity.getActivityId())
                                        .month(activityAccountMonthEntity.getMonth())
                                        .monthCount(activityAccountMonthEntity.getMonthCount())
                                        .monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1)
                                .build());
                        // 新创建月账户 则更新主表中月账户镜像额度
                        raffleActivityAccountDao.updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .monthCountSurplus(activityAccountEntity.getMonthCountSurplus()).build());
                    }

                    // 3. 创建或更新日账户
                    if (createPartakeOrderAggregate.isExistAccountDay()) {
                        int updateDayCount = raffleActivityAccountDayDao.updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .day(activityAccountDayEntity.getDay())
                                .build());
                        if (1 != updateDayCount) {
                            // 未更新成功则回滚
                            status.setRollbackOnly();
                            log.warn("写入创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {} day: {}", userId, activityId, activityAccountDayEntity.getDay());
                            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
                        }
                    } else {
                        raffleActivityAccountDayDao.insertActivityAccountDay(RaffleActivityAccountDay.builder()
                                .userId(activityAccountDayEntity.getUserId())
                                .activityId(activityAccountDayEntity.getActivityId())
                                .day(activityAccountDayEntity.getDay())
                                .dayCount(activityAccountDayEntity.getDayCount())
                                .dayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1)
                                .build());
                        // 新创建日账户，则更新总账表中日镜像额度 todo 新建表需要一个同步的操作
                        raffleActivityAccountDao.updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount.builder()
                                .userId(userId)
                                .activityId(activityId)
                                .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                                .build());
                    }
                    // 4. 写入参与活动订单
                    userRaffleOrderDao.insert(UserRaffleOrder.builder()
                            .userId(userRaffleOrderEntity.getUserId())
                            .activityId(userRaffleOrderEntity.getActivityId())
                            .activityName(userRaffleOrderEntity.getActivityName())
                            .strategyId(userRaffleOrderEntity.getStrategyId())
                            .orderId(userRaffleOrderEntity.getOrderId())
                            .orderTime(userRaffleOrderEntity.getOrderTime())
                            .orderState(userRaffleOrderEntity.getOrderState().getCode())
                            .build());
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入创建参与活动记录，唯一索引冲突 userId:{} activityId:{}", userId, activityId);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), ResponseCode.INDEX_DUP.getInfo());
                }
            });
        }
        finally {
            dbRouter.clear();
        }
    }

    @Override
    public UserRaffleOrderEntity queryNoUseRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        // 查询数据
        UserRaffleOrderEntity userRaffleOrderReq = new UserRaffleOrderEntity();
        String userId = partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        userRaffleOrderReq.setUserId(userId);
        userRaffleOrderReq.setActivityId(activityId);
        UserRaffleOrder userRaffleOrderRes = userRaffleOrderDao.queryNoUseRaffleOrder(userRaffleOrderReq);
        if (null == userRaffleOrderRes) return null;
        // 封装结果
        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userRaffleOrderRes.getUserId());
        userRaffleOrderEntity.setActivityId(userRaffleOrderRes.getActivityId());
        userRaffleOrderEntity.setActivityName(userRaffleOrderRes.getActivityName());
        userRaffleOrderEntity.setStrategyId(userRaffleOrderRes.getStrategyId());
        userRaffleOrderEntity.setOrderId(userRaffleOrderRes.getOrderId());
        userRaffleOrderEntity.setOrderTime(userRaffleOrderRes.getOrderTime());
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.valueOf(userRaffleOrderRes.getOrderState()));

        return userRaffleOrderEntity;
    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        RaffleActivityAccount raffleActivityAccountReq = new RaffleActivityAccount();
        raffleActivityAccountReq.setUserId(userId);
        raffleActivityAccountReq.setActivityId(activityId);
        RaffleActivityAccount raffleActivityAccountRes = raffleActivityAccountDao.queryActivityAccountByUserId(raffleActivityAccountReq);
        if (null == raffleActivityAccountRes) return null;

        return ActivityAccountEntity.builder()
                .userId(raffleActivityAccountRes.getUserId())
                .activityId(raffleActivityAccountRes.getActivityId())
                .totalCount(raffleActivityAccountRes.getTotalCount())
                .totalCountSurplus(raffleActivityAccountRes.getTotalCountSurplus())
                .monthCount(raffleActivityAccountRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountRes.getMonthCountSurplus())
                .dayCount(raffleActivityAccountRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountRes.getDayCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
        RaffleActivityAccountMonth raffleActivityAccountMonthReq = new RaffleActivityAccountMonth();
        raffleActivityAccountMonthReq.setUserId(userId);
        raffleActivityAccountMonthReq.setActivityId(activityId);
        raffleActivityAccountMonthReq.setMonth(month);
        RaffleActivityAccountMonth raffleActivityAccountMonthRes = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(raffleActivityAccountMonthReq);
        if (null == raffleActivityAccountMonthRes) return null;
        return ActivityAccountMonthEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .month(month)
                .monthCount(raffleActivityAccountMonthRes.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonthRes.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(day);
        RaffleActivityAccountDay raffleActivityAccountDayRes = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (null == raffleActivityAccountDayRes) return null;
        return ActivityAccountDayEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .day(day)
                .dayCount(raffleActivityAccountDayRes.getDayCount())
                .dayCountSurplus(raffleActivityAccountDayRes.getDayCountSurplus())
                .build();
    }

    @Override
    public List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId) {
        List<RaffleActivitySku> raffleActivitySkus = raffleActivitySkuDao.queryActivitySkuListByActivityId(activityId);
        ArrayList<ActivitySkuEntity> activitySkuEntities = new ArrayList<>(raffleActivitySkus.size());
        for (RaffleActivitySku raffleActivitySku : raffleActivitySkus) {
            Long sku = raffleActivitySku.getSku();
            ActivitySkuEntity activitySkuEntity = getActivitySkuEntity(raffleActivitySku, sku);
            activitySkuEntities.add(activitySkuEntity);
        }
        return activitySkuEntities;
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setActivityId(activityId);
        raffleActivityAccountDay.setUserId(userId);
        raffleActivityAccountDay.setDay(raffleActivityAccountDay.currentDay());
        Integer dayRaffleCount = raffleActivityAccountDayDao.queryRaffleActivityAccountDayPartakeCount(raffleActivityAccountDay);
        return null == dayRaffleCount ? 0 : dayRaffleCount;
    }

    @Override
    public ActivityAccountEntity queryUserActivityAccount(Long activityId, String userId) {
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(RaffleActivityAccount.builder()
                .userId(userId)
                .activityId(activityId).build());
        if (null == raffleActivityAccount) {
            return ActivityAccountEntity.builder()
                    .activityId(activityId)
                    .userId(userId)
                    .totalCount(0)
                    .totalCountSurplus(0)
                    .monthCount(0)
                    .monthCountSurplus(0)
                    .dayCount(0)
                    .dayCountSurplus(0).build();
        }
        RaffleActivityAccountMonth raffleActivityAccountMonth = raffleActivityAccountMonthDao.queryActivityAccountMonthByUserId(RaffleActivityAccountMonth.builder()
                .userId(userId)
                .activityId(activityId).build());
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(RaffleActivityAccountDay.builder()
                .userId(userId)
                .activityId(activityId).build());
        // 组装对象
        ActivityAccountEntity activityAccountEntity = new ActivityAccountEntity();
        activityAccountEntity.setUserId(userId);
        activityAccountEntity.setActivityId(activityId);
        activityAccountEntity.setTotalCount(raffleActivityAccount.getTotalCount());
        activityAccountEntity.setTotalCountSurplus(raffleActivityAccount.getTotalCountSurplus());

        if (null == raffleActivityAccountDay) {
            activityAccountEntity.setDayCount(raffleActivityAccount.getDayCount());
            activityAccountEntity.setDayCountSurplus(raffleActivityAccount.getDayCountSurplus());
        }else {
            activityAccountEntity.setDayCount(raffleActivityAccountDay.getDayCount());
            activityAccountEntity.setDayCountSurplus(raffleActivityAccountDay.getDayCountSurplus());
        }

        if (null == raffleActivityAccountMonth) {
            activityAccountEntity.setMonthCount(raffleActivityAccount.getMonthCount());
            activityAccountEntity.setMonthCountSurplus(raffleActivityAccount.getMonthCountSurplus());
        } else {
            activityAccountEntity.setMonthCount(raffleActivityAccountMonth.getMonthCount());
            activityAccountEntity.setMonthCountSurplus(raffleActivityAccountMonth.getMonthCountSurplus());
        }

        return activityAccountEntity;
    }

    @Override
    public Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId) {
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountDao.queryActivityAccountByUserId(RaffleActivityAccount.builder()
                .userId(userId)
                .activityId(activityId).build());
        return raffleActivityAccount.getTotalCount() - raffleActivityAccount.getTotalCountSurplus();
    }

    private static ActivitySkuEntity getActivitySkuEntity(RaffleActivitySku raffleActivitySku, Long sku) {
        Long activityId = raffleActivitySku.getActivityId();
        Long activityCountId = raffleActivitySku.getActivityCountId();
        Integer stockCount = raffleActivitySku.getStockCount();
        Integer stockCountSurplus = raffleActivitySku.getStockCountSurplus();
        ActivitySkuEntity activitySkuEntity = new ActivitySkuEntity();
        activitySkuEntity.setSku(sku);
        activitySkuEntity.setActivityId(activityId);
        activitySkuEntity.setActivityCountId(activityCountId);
        activitySkuEntity.setStockCount(stockCount);
        activitySkuEntity.setStockCountSurplus(stockCountSurplus);
        return activitySkuEntity;
    }
}
