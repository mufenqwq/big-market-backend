package site.mufen.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mufen
 * @Description
 * @create 2024/11/30 22:43
 */
@Configuration
@EnableConfigurationProperties(ZookeeperClientConfigProperties.class)
public class ZookeeperClientConfig {

    /**
     * 多参数构建ZooKeeper客户端连接
     *
     * @return client
     */
    @Bean(name = "zookeeperClient")
    @ConditionalOnProperty(value = "zookeeper.sdk.config.enable", havingValue = "true")
    public CuratorFramework createWithOptions(ZookeeperClientConfigProperties properties) {
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(properties.getConnectString())
            .retryPolicy(backoffRetry)
            .sessionTimeoutMs(properties.getSessionTimeoutMs())
            .connectionTimeoutMs(properties.getConnectionTimeoutMs())
            .build();
        client.start();
        return client;
    }


}
