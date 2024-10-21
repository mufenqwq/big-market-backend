package site.mufen.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import site.mufen.domain.strategy.service.rule.tree.ILogicTreeNode;
import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author mufen
 * @Description 库存节点 真正得要减少库存
 * @create 2024/10/21 15:40
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {



    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long StrategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER).build();
    }
}
