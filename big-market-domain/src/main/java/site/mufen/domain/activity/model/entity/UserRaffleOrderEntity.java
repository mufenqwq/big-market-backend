package site.mufen.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.activity.model.valobj.UserRaffleOrderStateVO;

import java.util.Date;

/**
 * @author mufen
 * @Description 用户抽奖订单实体对象
 * @create 2024/11/4 15:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrderEntity {
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
    private UserRaffleOrderStateVO orderState;
    /**
     * 活动结束时间
     */
    private Date endDateTime;
}
