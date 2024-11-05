package site.mufen.domain.activity.service;

import site.mufen.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * @author mufen
 * @Description 活动sku库存处理接口
 * @create 2024/11/3 23:17
 */
public interface IRaffleActivitySkuStockService {

    /**
     * 获取活动sku库存消耗队列
     * @return 奖品库存key信息
     */
    ActivitySkuStockKeyVO takeQueueValue();

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 延迟队列 + 任务趋势更新活动sku库存
     * @param sku 活动商品
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 缓存库存已经消耗完毕 清空数据库库存
     * @param sku 活动商品
     */
    void clearActivitySkuStock(Long sku);

}
