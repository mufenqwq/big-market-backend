package site.mufen.test.infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.infrastructure.persistent.dao.IRaffleActivityDao;
import site.mufen.infrastructure.persistent.po.RaffleActivity;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 抽奖活动配置Dao类
 * @create 2024/10/31 21:31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleActivityDaoTest {

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Test
    public void test_queryRaffleActivityByActivityId() {
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(100301L);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivity));
    }
}
