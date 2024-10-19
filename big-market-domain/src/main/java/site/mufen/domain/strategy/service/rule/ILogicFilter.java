package site.mufen.domain.strategy.service.rule;

import site.mufen.domain.strategy.model.entity.RuleActionEntity;
import site.mufen.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author mufen
 * @Description 抽奖规则过滤接口
 * @create 2024/10/17 20:43
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
