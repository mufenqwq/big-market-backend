package site.mufen.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import site.mufen.domain.strategy.service.rule.tree.ILogicTreeNode;
import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author mufen
 * @Description 次数锁节点
 * @create 2024/10/21 15:38
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {


    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long StrategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
