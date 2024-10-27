package site.mufen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 抽奖奖品实体
 * @create 2024/10/17 20:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {

    /**
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;
    /**
     * 奖品配置信息
     */
    private String awardConfig;
    /**
     * 奖品顺序号
     */
    private Integer sort;
}
