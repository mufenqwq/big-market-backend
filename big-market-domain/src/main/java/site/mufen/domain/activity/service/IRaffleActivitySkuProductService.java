package site.mufen.domain.activity.service;

import site.mufen.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @author mufen
 * @Description sku商品服务接口
 * @create 2024/11/15 15:45
 */
public interface IRaffleActivitySkuProductService {

    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);

}
