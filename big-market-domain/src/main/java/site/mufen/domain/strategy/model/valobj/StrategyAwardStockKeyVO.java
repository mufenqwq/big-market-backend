package site.mufen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 策略奖品库存key标识值id
 * @create 2024/10/23 19:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {
    // 策略Id
    private Long strategyId;
    // 奖品Id
    private Integer awardId;
}
