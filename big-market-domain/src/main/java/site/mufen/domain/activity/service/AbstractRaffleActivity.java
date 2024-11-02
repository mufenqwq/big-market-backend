package site.mufen.domain.activity.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import site.mufen.domain.activity.model.entity.*;
import site.mufen.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 抽奖活动抽象类 定义标准的流程
 * @create 2024/11/1 19:47
 */
@Slf4j
public abstract class AbstractRaffleActivity implements IRaffleOrder {
    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }

}
