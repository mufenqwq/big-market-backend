package site.mufen.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.mufen.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import site.mufen.domain.activity.service.ISkuStock;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 更新活动sku库存
 * @create 2024/11/4 01:04
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {

    @Resource
    private ISkuStock skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("定时任务， 更新活动sku库存【延迟队列获取，降低对数据库的更新频率，不要产生竞争】");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (null == activitySkuStockKeyVO) return;
            log.info("定时任务， 更新活动sku库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务， 更新活动sku库存失败", e);
        }
    }
}
