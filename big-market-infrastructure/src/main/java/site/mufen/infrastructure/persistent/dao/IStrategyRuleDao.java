package site.mufen.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.persistent.po.StrategyRule;

import java.util.List;

/**
 * @author mufen
 * @Description 策略规则 dao
 * @create 2024/10/15 15:16
 */
@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule>  queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule strategyRule);
}
