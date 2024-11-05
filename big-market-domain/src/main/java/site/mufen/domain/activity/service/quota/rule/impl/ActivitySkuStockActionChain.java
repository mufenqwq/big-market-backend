package site.mufen.domain.activity.service.quota.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.entity.ActivityCountEntity;
import site.mufen.domain.activity.model.entity.ActivityEntity;
import site.mufen.domain.activity.model.entity.ActivitySkuEntity;
import site.mufen.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.armory.IActivityDispatch;
import site.mufen.domain.activity.service.quota.rule.AbstractActionChain;
import site.mufen.types.enums.ResponseCode;
import site.mufen.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 商品库存规则节点
 * @create 2024/11/2 18:33
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。sku:{}, activityId:{}", activitySkuEntity.getSku(), activitySkuEntity.getActivityId());
        // 扣减库存
        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());

        // true 扣减库存成功
        if (status) {
            log.info("活动责任链-商品库存处理【校验&扣减】成功。sku:{}, activityId:{}", activitySkuEntity.getSku(), activitySkuEntity.getActivityId());

            // 写入延迟队列 延迟消费更新库存记录
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId()).build());

            return true;
        }

        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERR.getInfo());
    }
}
