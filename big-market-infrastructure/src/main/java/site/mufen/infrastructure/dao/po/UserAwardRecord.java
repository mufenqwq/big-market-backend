package site.mufen.infrastructure.persistent.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * @author mufen
 * @Description 用户中奖记录表po
 * @create 2024/11/4 10:34
 */
@Data
public class UserAwardRecord {
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
     * 策略Id
     */
    private Long strategyId;
    /**
     * 抽奖订单id (作为幂等使用
     */
    private String orderId;
    /**
     * 奖品Id
     */
    private Integer awardId;
    /**
     * 奖品标题
     */
    private String awardTitle;
    /**
     * 中奖时间
     */
    private Date awardTime;
    /**
     * 奖品状态 create-创建 completed-发奖完成
     */
    private String awardState;
    private Date createTime;
    private Date updateTime;
}
