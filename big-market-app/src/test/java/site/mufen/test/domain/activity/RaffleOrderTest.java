package site.mufen.test.domain.activity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.domain.activity.model.entity.ActivityOrderEntity;
import site.mufen.domain.activity.model.entity.ActivityShopCartEntity;
import site.mufen.domain.activity.service.IRaffleOrder;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 抽奖活动订单单测
 * @create 2024/11/1 20:50
 */
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RaffleOrderTest {

    @Resource
    private IRaffleOrder raffleOrder;

    @Test
    public void test_createRaffleActivityOrder() {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("xiaofuge");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrder));
    }

}
