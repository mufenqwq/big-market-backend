package site.mufen.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mufen
 * @Description 抽奖应答对象
 * @create 2024/10/25 01:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleStrategyResponseDTO implements Serializable {
    // 奖品Id
    private Integer awardId;
    // 排序编号
    private Integer awardIndex;
}
