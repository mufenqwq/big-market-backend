package site.mufen.domain.strategy.service.rule.tree;

import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author mufen
 * @Description 规则树接口
 * @create 2024/10/21 15:35
 */
public interface ILogicTreeNode {


    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime);
}
