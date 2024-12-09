package site.mufen.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mufen
 * @Description 用户抽奖记录表
 * @create 2024/11/4 10:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrder {
    /**
     * 自增id
     */
    private String id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 策略Id
     */
    private Long strategyId;
    /**
     * 抽奖订单id (作为幂等使用
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /** 订单状态；create-创建、used-已使用、cancel-已作废 */
    private String orderState;
    private Date createTime;
    private Date updateTime;
}
