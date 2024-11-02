package site.mufen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mufen
 * @Description  抽奖活动账单 持久化对象
 * @create 2024/10/29 11:03
 */
@Data
public class RaffleActivityOrder {
    /**
     * 自增ID
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date  updateTime;

}
