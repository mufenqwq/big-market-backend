package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mufen
 * @Description sku商品实体
 * @create 2024/11/15 15:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuProductEntity {
    private Long sku;
    private Long activityId;
    /**
     * 活动个人参与次数Id
     */
    private Long activityCountId;
    /**
     * 总库存
     */
    private Integer stockCount;
    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;
    /**
     * 商品金额
     */
    private BigDecimal productAmount;
    /**
     * 活动配置的次数 - 购买商品后可以获得的次数
     */
    private ActivityCount activityCount;


    @Data
    public static class ActivityCount {
        private Integer totalCount;
        private Integer monthCount;
        private Integer dayCount;
    }
}
