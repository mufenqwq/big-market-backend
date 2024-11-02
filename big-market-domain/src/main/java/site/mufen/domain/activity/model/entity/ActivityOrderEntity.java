package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mufen
 * @Description 活动订单实体
 * @create 2024/11/1 19:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOrderEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 月次数
     */
    private Integer monthCount;
    /**
     * 订单状态（not_used、used、expire）
     */
    private String state;
}