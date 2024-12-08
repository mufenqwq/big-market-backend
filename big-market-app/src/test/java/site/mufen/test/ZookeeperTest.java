package site.mufen.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author mufen
 * @Description
 * @create 2024/11/30 22:46
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperTest {

    @Resource
    private CuratorFramework curatorFramework;

    @Test
    public void createNode() throws Exception {
        String path = "/big-market-doc/config/downgradeSwitch/test/a";
        String data = "0";
       if (null == curatorFramework.checkExists().forPath(path)) {
           curatorFramework.create().creatingParentsIfNeeded().forPath(path);
       }
    }

    @Test
    public void setData() throws Exception {
        curatorFramework.setData().forPath("/big-market-doc/config/downgradeSwitch/test/a", "111".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void getData() throws Exception {
        String downgradeSwitch = new String(curatorFramework.getData().forPath("/big-market-doc/config/downgradeSwitch/test/a"), StandardCharsets.UTF_8);
        log.info("测试结果: {}", downgradeSwitch);
    }
}
