package site.mufen.domain.strategy.service.rule.tree.factory.engine;

import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author mufen
 * @Description 决策树引擎接口
 * @create 2024/10/21 16:26
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);
}
