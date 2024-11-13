package site.mufen.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.model.entity.StrategyEntity;
import site.mufen.domain.strategy.repository.IStrategyRepository;
import site.mufen.domain.strategy.service.rule.chain.ILogicChain;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mufen
 * @Description 责任链默认工厂
 * @create 2024/10/21 12:20
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> loginChainGroup;


    private IStrategyRepository repository;

    @Autowired
    public DefaultChainFactory(Map<String, ILogicChain> loginChainGroup, IStrategyRepository repository) {
        this.loginChainGroup = loginChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.ruleModels();

        if (null == ruleModels || ruleModels.length == 0) return loginChainGroup.get("default");

        ILogicChain logicChain = loginChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain next = loginChainGroup.get(ruleModels[i]);
            current = current.appendNext(next);
        }

        current.appendNext(loginChainGroup.get("default"));

        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /**
         * 奖品Id
         */
        private Integer awardId;
        /**
         * 抽奖类型：黑名单抽奖，权重抽奖，默认抽奖
         */
        private String logicModel;
        /**
         * 抽奖奖品规则
         */
        private String awardRuleValue;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重抽奖"),
        ;
        private final String code;
        private final String info;
    }


}
