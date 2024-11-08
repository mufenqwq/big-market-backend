package site.mufen.domain.rebate.service;

import site.mufen.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @author mufen
 * @Description 行为返利服务接口
 * @create 2024/11/8 22:24
 */
public interface IBehaviorRebateService {

    /**
     * 行为的入账订单
     * @param behaviorEntity 行为动作实体
     * @return 订单Id集合
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);
}
