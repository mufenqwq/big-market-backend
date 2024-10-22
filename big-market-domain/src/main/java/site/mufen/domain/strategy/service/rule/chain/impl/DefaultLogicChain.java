package site.mufen.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.mufen.domain.strategy.service.armory.IStrategyDispatch;
import site.mufen.domain.strategy.service.rule.chain.AbstractLogicChain;
import site.mufen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 兜底
 * @create 2024/10/21 11:19
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain  {

    @Resource
    protected IStrategyDispatch strategyDispatch;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
        return DefaultChainFactory.StrategyAwardVO.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
    }
}
