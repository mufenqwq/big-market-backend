package site.mufen.trigger.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author mufen
 * @Description sku商品对象
 * @create 2024/11/15 13:24
 */
@Data
public class SkuProductResponseDTO implements Serializable {
    /**
     * 自增Id
     */
    private Long id;

    /**
     * 商品sku
     */
    private Long sku;

    /**
     * 活动Id
     */
    private Long activityId;

    /**
     * 活动个人次数参与Id
     */
    private Long activityCountId;

    /**
     * 库存总量
     */
    private Integer stockCount;

    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;

    /**
     * 商品金额 [积分]
     */
    private BigDecimal productAmount;
    /**
     * 活动商品数量
      */
    private ActivityCount activityCount;
    @Data
    public static class ActivityCount {
        private Integer totalCount;
        private Integer monthCount;
        private Integer dayCount;
    }
}
