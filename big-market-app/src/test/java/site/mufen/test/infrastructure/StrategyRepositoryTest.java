package site.mufen.test.infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.mufen.domain.strategy.model.valobj.RuleTreeVO;
import site.mufen.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;

/**
 * @author mufen
 * @Description 仓储服务测试
 * @create 2024/10/22 09:37
 */
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class StrategyRepositoryTest {

    @Resource
    private IStrategyRepository repository;

    @Test
    public void queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("测试结果: {}", JSON.toJSONString(ruleTreeVO));
    }
}
