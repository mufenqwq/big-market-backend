package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 活动购物车实体
 * @create 2024/11/1 19:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityShopCartEntity {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 商品SKU - activity + activity count
     */
    private Long sku;
}
