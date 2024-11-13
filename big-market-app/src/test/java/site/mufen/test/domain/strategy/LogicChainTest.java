package site.mufen.test.domain.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import site.mufen.domain.strategy.service.armory.IStrategyArmory;
import site.mufen.domain.strategy.service.rule.chain.ILogicChain;
import site.mufen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import site.mufen.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 抽奖责任链测试 验证不同的规则走不同的责任链
 * @create 2024/10/21 12:51
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class LogicChainTest {
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private DefaultChainFactory defaultChainFactory;

    @Before
    public void setUp() {
        // 策略装配 100001、100002、100003
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100002L));
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100003L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
    }

    @Test
    public void test_LogicChain_rule_blacklist() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("user001", 100001L);
        log.info("测试结果：{}", strategyAwardVO);
    }

    @Test
    public void test_LogicChain_rule_weight() {
        // 通过反射 mock 规则中的值
        //ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4900L);

        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("xiaofuge", 100001L);
        log.info("测试结果：{}", strategyAwardVO);
    }

    @Test
    public void test_LogicChain_rule_default() {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(100001L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("xiaofuge", 100001L);
        log.info("测试结果：{}", strategyAwardVO);
    }
}
