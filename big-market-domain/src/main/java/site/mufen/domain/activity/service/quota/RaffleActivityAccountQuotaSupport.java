package site.mufen.domain.activity.service.quota;

import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

/**
 * @author mufen
 * @Description
 * @create 2024/11/2 18:21
 */
public class RaffleActivityAccountQuotaSupport {

    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository activityRepository;

    public RaffleActivityAccountQuotaSupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }


    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
