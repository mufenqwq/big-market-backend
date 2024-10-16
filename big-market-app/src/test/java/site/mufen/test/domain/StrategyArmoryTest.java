package site.mufen.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.domain.strategy.service.armory.IStrategyArmory;

import javax.annotation.Resource;
import javax.swing.*;

/**
 * @author mufen
 * @Description
 * @create 2024/10/17 01:17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Test
    public void test_strategyArmory() {
        boolean flag = strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void test_getAssembleRandomVal() {
        log.info("测试结果: {} - 奖品ID值", strategyArmory.getRandomAwardId(100002L));
        log.info("测试结果: {} - 奖品ID值", strategyArmory.getRandomAwardId(100002L));
        log.info("测试结果: {} - 奖品ID值", strategyArmory.getRandomAwardId(100002L));
    }

}
