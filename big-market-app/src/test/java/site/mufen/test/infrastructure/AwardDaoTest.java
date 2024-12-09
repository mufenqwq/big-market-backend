package site.mufen.test.infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.infrastructure.dao.IAwardDao;
import site.mufen.infrastructure.dao.IStrategyAwardDao;
import site.mufen.infrastructure.dao.IStrategyDao;
import site.mufen.infrastructure.dao.IStrategyRuleDao;
import site.mufen.infrastructure.dao.po.Award;
import site.mufen.infrastructure.dao.po.Strategy;
import site.mufen.infrastructure.dao.po.StrategyAward;
import site.mufen.infrastructure.dao.po.StrategyRule;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mufen
 * @Description 奖品持久化单元测试
 * @create 2024/10/15 16:59
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("测试结果: {}", JSON.toJSONString(awards));
    }

    @Test
    public void test_queryStrategyAwardList() {
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        log.info("测试结果: {}", JSON.toJSONString(strategyAwards));
    }

    @Test
    public void test_queryStrategyList() {
        List<Strategy> strategies = strategyDao.queryStrategyList();
        log.info("测试结果: {}", JSON.toJSONString(strategies));
    }

    @Test
    public void test_queryStrategyRuleList() {
        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
        log.info("测试结果: {}", JSON.toJSONString(strategyRules));
    }
}
