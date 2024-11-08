package site.mufen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mufen
 * @Description 抽奖因子对象
 * @create 2024/10/17 20:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleFactorEntity {

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 策略Id
     */
    private Long strategyId;
    /**
     * 奖品Id
     */
    private Integer awardId;
    /**
     * 结束时间
     */
    private Date endDateTime;
}
