package site.mufen.domain.strategy.service;

import java.util.Map;

/**
 * @author mufen
 * @Description 抽奖规则接口
 * @create 2024/11/7 22:13
 */
public interface IRaffleRule {

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

}
