package site.mufen.domain.activity.service.rule;

import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author mufen
 * @Description 下蛋规则过滤责任链
 * @create 2024/11/2 18:25
 */
public interface IActionChain extends IActionChainArmory {
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity,
                   ActivityCountEntity activityCountEntity);
}
