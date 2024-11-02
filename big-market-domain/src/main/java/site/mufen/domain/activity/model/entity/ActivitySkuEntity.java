package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 活动sku实体
 * @create 2024/11/1 19:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySkuEntity {

    /**
     * 商品sku
     */
    private Long sku;
    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 活动个人参数ID；在这个活动上，一个人可参与多少次活动（总、日、月）
     */
    private Long activityCountId;
    /**
     * 库存总量
     */
    private Integer stockCount;
    /**
     * 库存剩余量
     */
    private Integer stockCountSurplus;
}
