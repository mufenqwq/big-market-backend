package site.mufen.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.infrastructure.redis.IRedisService;
import site.mufen.trigger.api.IRaffleActivityService;
import site.mufen.trigger.api.dto.ActivityDrawRequestDTO;
import site.mufen.trigger.api.dto.ActivityDrawResponseDTO;
import site.mufen.types.model.Response;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IRedisService redisService;

    @Test
    public void test() {
        log.info("测试完成");
    }


    @Test
    public void test_redis() {
        RMap<Object, Object> map = redisService.getMap("strategy_id_100001");
        map.put(1, 101);
        map.put(2, 102);

        map.put(3, 103);
        map.put(4, 104);

        map.put(5, 105);
        map.put(6, 106);
        map.put(7, 107);
        map.put(8, 108);
        map.put(9, 109);
        map.put(10, 1010);
        log.info("测试结果: {}", redisService.getFromMap("strategy_id_100001", 1).toString());

    }

    @DubboReference(interfaceClass = IRaffleActivityService.class, version = "1.0")
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_rpc() {
        ActivityDrawRequestDTO request = new ActivityDrawRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("xiaofuge");
        Response<ActivityDrawResponseDTO> response = raffleActivityService.draw(request);

        log.info("请求参数：{}", JSON.toJSONString(request));
        log.info("测试结果：{}", JSON.toJSONString(response));
    }
}
