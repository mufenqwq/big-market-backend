package site.mufen.infrastructure.persistent.repository;

import org.springframework.stereotype.Repository;
import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;
import site.mufen.domain.activity.model.valobj.ActivityStateVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.infrastructure.persistent.dao.IRaffleActivityCountDao;
import site.mufen.infrastructure.persistent.dao.IRaffleActivityDao;
import site.mufen.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import site.mufen.infrastructure.persistent.po.RaffleActivity;
import site.mufen.infrastructure.persistent.po.RaffleActivityCount;
import site.mufen.infrastructure.persistent.po.RaffleActivitySku;
import site.mufen.infrastructure.persistent.redis.IRedisService;
import site.mufen.types.common.Constants;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 活动仓库实现
 * @create 2024/11/1 20:33
 */
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
}
