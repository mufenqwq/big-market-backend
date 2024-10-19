package site.mufen.domain.strategy.service.rule.factory;


import com.alibaba.fastjson2.util.AnnotationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import site.mufen.domain.strategy.annotation.LogicStrategy;
import site.mufen.domain.strategy.model.entity.RuleActionEntity;
import site.mufen.domain.strategy.service.rule.ILogicFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mufen
 * @Description 规则工厂
 * @create 2024/10/18 00:59
 */
@Service
public class DefaultLogicFactory {

    public ConcurrentHashMap<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_WEIGHT("rule_weight", "[抽奖前规则] 根据抽奖权重返回可抽奖范围KEY"),
        RULE_BLACKLIST("rule_blacklist", "[抽奖前规则] 黑名单规则过滤， 命中黑名单则直接返回")
        ;

        private final  String code;
        private final String info;
    }
}
