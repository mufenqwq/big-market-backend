package site.mufen.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mufen
 * @Description 抽奖奖品列表 应答对象
 * @create 2024/10/25 01:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseDTO {

    // 奖品Id
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则 - 抽奖N次后解锁 为配置则为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 - true 解锁 false 未解锁
    private Boolean isAwardUnlock;
    // 解锁还需要的次数
    private Integer waitUnlockCount;

}
