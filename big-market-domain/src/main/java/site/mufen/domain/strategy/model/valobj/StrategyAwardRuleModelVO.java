package site.mufen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author mufen
 * @Description 抽奖策略规则值对象 没有唯一ID 仅限于从数据库查询对象
 * @create 2024/10/20 15:32
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

}
