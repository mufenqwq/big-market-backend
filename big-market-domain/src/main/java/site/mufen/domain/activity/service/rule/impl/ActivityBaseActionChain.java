package site.mufen.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;
import site.mufen.domain.activity.service.rule.AbstractActionChain;

/**
 * @author mufen
 * @Description 活动规则过滤 【日期 ，状态】
 * @create 2024/11/2 18:32
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期， 状态】校验开始");

        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
