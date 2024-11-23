package site.mufen.domain.activity.service.product;

import org.springframework.stereotype.Service;
import site.mufen.domain.activity.model.entity.SkuProductEntity;
import site.mufen.domain.activity.repository.IActivityRepository;
import site.mufen.domain.activity.service.IRaffleActivitySkuProductService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author mufen
 * @Description sku商品服务
 * @create 2024/11/15 16:02
 */
@Service
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return activityRepository.querySkuProductEntityListByActivityId(activityId);
    }
}
