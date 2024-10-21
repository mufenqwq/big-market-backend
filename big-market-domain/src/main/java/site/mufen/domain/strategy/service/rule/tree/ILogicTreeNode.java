package site.mufen.domain.strategy.service.rule.tree;

import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author mufen
 * @Description 规则树接口
 * @create 2024/10/21 15:35
 */
public interface ILogicTreeNode {


    DefaultTreeFactory.TreeActionEntity logic(String userId, Long StrategyId, Integer awardId);
}
