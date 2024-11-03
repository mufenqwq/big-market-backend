package site.mufen.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 活动 sku key 库存值对象
 * @create 2024/11/3 19:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySkuStockKeyVO {

    /**
     * 商品sku
     */
    private Long sku;
    /**
     * 活动id
     */
    private Long activityId;
}
