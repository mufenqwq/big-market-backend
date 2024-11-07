package site.mufen.domain.activity.service;

import site.mufen.domain.activity.model.entity.PartakeRaffleActivityEntity;
import site.mufen.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @author mufen
 * @Description 抽奖活动参与服务
 * @create 2024/11/4 15:12
 */
public interface IRaffleActivityPartakeService {
    /**
     * 创建抽奖单 用户参与抽奖活动 扣减活动账户库存 产生抽奖单，如存在未被使用的抽奖单则直接返回
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);

    UserRaffleOrderEntity createOrder(String userId, Long activityId);
}
