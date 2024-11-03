package site.mufen.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;
import site.mufen.domain.activity.model.valobj.ActivityStateVO;
import site.mufen.domain.activity.service.rule.AbstractActionChain;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import java.util.Date;

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
        log.info("活动责任链-基础信息【有效期， 状态】校验开始: sku:{}, activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        // 1. 校验活动状态
        if (!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        // 2.校验: 活动日期 [开始时间 - 当前时间 - 结束时间]
        Date currentDate = new Date();
        if (activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        // 3. 校验： 活动sku库存 [剩余库存从缓存中获取]
        if (activitySkuEntity.getStockCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERR.getInfo());
        }
        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
