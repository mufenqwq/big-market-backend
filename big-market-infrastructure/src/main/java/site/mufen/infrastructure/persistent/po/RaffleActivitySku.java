package site.mufen.infrastructure.persistent.po;

import lombok.Data;
import lombok.Getter;

import java.util.Date;


/**
 * @author mufen
 * @Description 抽奖活动sku的持久化对象
 * @create 2024/11/1 18:15
 */
@Data
@Getter
public class RaffleActivitySku {
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

    private Date createTime;

    private Date updateTime;
}
