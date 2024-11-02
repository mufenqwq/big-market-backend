package site.mufen.domain.activity.service;

import site.mufen.domain.activity.model.entity.ActivityOrderEntity;
import site.mufen.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @author mufen
 * @Description 抽奖活动订单接口
 * @create 2024/11/1 18:45
 */
public interface IRaffleOrder {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格 (可消耗的次数
     * @param activityShopCartEntity 活动sku实体 通过sku领取活动
     * @return 活动参与记录实体
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
