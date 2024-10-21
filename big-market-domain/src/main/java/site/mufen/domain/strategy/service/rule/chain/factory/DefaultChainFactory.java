package site.mufen.domain.strategy.service.rule.chain.factory;

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
}
