package site.mufen.domain.strategy.service.raffle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.model.entity.RaffleFactorEntity;
import site.mufen.domain.strategy.model.entity.RuleActionEntity;
import site.mufen.domain.strategy.model.entity.RuleMatterEntity;
import site.mufen.domain.strategy.model.entity.StrategyAwardEntity;
import site.mufen.domain.strategy.model.valobj.*;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.domain.strategy.service.AbstractRaffleStrategy;
import site.mufen.domain.strategy.service.IRaffleAward;
import site.mufen.domain.strategy.service.IRaffleRule;
import site.mufen.domain.strategy.service.IRaffleStock;
import site.mufen.domain.strategy.service.armory.IStrategyDispatch;
import site.mufen.domain.strategy.service.rule.chain.ILogicChain;
import site.mufen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

import site.mufen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import site.mufen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import javax.annotation.Resource;
import java.util.*;

import java.util.stream.Collectors;

/**
 * @author mufen
 * @Description
 * @create 2024/10/19 13:50
 */
@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward, IRaffleRule {



    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch,
                                 DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory ) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        return raffleLogicTree(userId, strategyId, awardId, null);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId, Date endDateTime) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModel(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId, endDateTime);
    }


    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return repository.queryStrategyAwardList(strategyId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return repository.queryAwardRuleLockCount(treeIds);
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeight(Long strategyId) {
        return repository.queryAwardRuleWeight(strategyId);
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return queryAwardRuleWeight(strategyId);
    }
}
