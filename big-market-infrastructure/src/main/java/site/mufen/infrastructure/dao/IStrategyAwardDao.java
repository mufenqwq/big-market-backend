package site.mufen.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import site.mufen.infrastructure.dao.po.StrategyAward;

import java.util.List;

/**
 * @author mufen
 * @Description 抽奖策略奖品的明细配置 dao
 * @create 2024/10/15 15:16
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    /**
     * 更新库存
     * @param strategyAward 策略奖品对象
     */
    void updateStrategyAwardStock(StrategyAward strategyAward);

    /**
     * 查询 strategyAward
     * @param strategyAwardReq 请求 strategyAward参数
     * @return strategyAward对象
     */
    StrategyAward queryStrategyAward(StrategyAward strategyAwardReq);

    /**
     *
     * @return
     */
    List<StrategyAward> queryOpenActivityStrategyAwardList();
}
