package site.mufen.test.domain.strategy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.domain.strategy.model.entity.RaffleAwardEntity;
import site.mufen.domain.strategy.model.entity.RaffleFactorEntity;
import site.mufen.domain.strategy.service.IRaffleStrategy;
import site.mufen.domain.strategy.service.armory.IStrategyArmory;



import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author mufen
 * @Description
 * @create 2024/10/19 18:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleStrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;


    @Before
    public void setUp() {
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100002L));
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100003L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));

    }

    @Test
    public void test_performRaffle() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("mufen")
                    .strategyId(100006L)
                    .build();

            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

            log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
        new CountDownLatch(1).await();
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_raffle_center_rule_lock() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("mufen")
                .strategyId(100003L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }
}
