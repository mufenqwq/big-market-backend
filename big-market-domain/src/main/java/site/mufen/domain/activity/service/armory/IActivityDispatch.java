package site.mufen.domain.activity.service.armory;

import java.util.Date;

/**
 * @author mufen
 * @Description 活动调度 【扣减库存】
 * @create 2024/11/3 18:29
 */
public interface IActivityDispatch {

    /**
     * 根据策略id和奖品id 扣减奖品缓存库存
     * @param sku 互动sku
     * @param endDateTime 活动结束时间 根据结束时间设置加锁的key的结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);

}
