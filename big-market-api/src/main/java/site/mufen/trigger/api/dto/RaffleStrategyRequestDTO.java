package site.mufen.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mufen
 * @Description 抽奖请求参数
 * @create 2024/10/25 01:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyRequestDTO implements Serializable {
    // 抽奖策略Id
    private Long strategyId;
}
