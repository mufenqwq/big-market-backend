package site.mufen.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.mufen.domain.award.model.valobj.AwardStateVO;

import java.util.Date;

/**
 * @author mufen
 * @Description 用户中奖记录实体对象
 * @create 2024/11/5 16:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecordEntity {

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
    private AwardStateVO awardState;
}
