package site.mufen.domain.strategy.service.rule.tree.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import site.mufen.domain.strategy.model.vo.RuleTreeVO;
import site.mufen.domain.strategy.service.rule.tree.ILogicTreeNode;
import site.mufen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import site.mufen.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;

import java.util.Map;

/**
 * @author mufen
 * @Description 规则树工厂
 * @create 2024/10/21 15:42
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    @Autowired
    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckTypeVO;
        private StrategyAwardData strategyAwardData;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardData {
        /**
         * 奖品Id
         */
        private Integer awardId;
        /**
         * 抽奖奖品规则
         */
        private String awardRuleValue;
    }
}
