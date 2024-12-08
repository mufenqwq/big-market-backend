package site.mufen.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import site.mufen.types.annotations.DCCValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mufen
 * @Description 基于Zookeeper的配置中心实现原理
 * @create 2024/12/1 00:39
 */
@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {
    private static final String BASE_CONFIG_PATH = "/big-market-dcc";
    private static final String BASE_CONFIG_PATH_CONFIG = BASE_CONFIG_PATH + "/config";

    private final CuratorFramework client;

    private final Map<String, Object> dccObjGroup = new HashMap<>();

    public DCCValueBeanFactory(CuratorFramework client) throws Exception {
        this.client = client;

        if (null == client.checkExists().forPath(BASE_CONFIG_PATH_CONFIG)) {
            client.create().creatingParentsIfNeeded().forPath(BASE_CONFIG_PATH_CONFIG);
            log.info("DCC 节点监听 base node {} not absent create new done!", BASE_CONFIG_PATH_CONFIG);
        }

        CuratorCache curatorCache = CuratorCache.build(client, BASE_CONFIG_PATH_CONFIG);
        curatorCache.start();

        curatorCache.listenable().addListener((type, oldData, data) -> {
            switch (type) {
                case NODE_CHANGED:
                    String dccValuePath = data.getPath();
                    Object objBean = dccObjGroup.get(dccValuePath);
                    if (null == objBean) return;
                    try {
                        Field field = objBean.getClass().getDeclaredField(dccValuePath.substring(dccValuePath.lastIndexOf("/") + 1));
                        field.setAccessible(true);
                        field.set(objBean, new String(data.getData()));
                        field.setAccessible(false);
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DCCValue.class)) continue;
            DCCValue dccValue = field.getAnnotation(DCCValue.class);

            String value = dccValue.value();
            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case 「isSwitch/isSwitch:1」");
            }

            String[] splits = value.split(":");
            String key = splits[0];
            String defaultValue = splits.length == 2 ? splits[1] : null;

            String keyPath = BASE_CONFIG_PATH_CONFIG.concat("/").concat(key);
            try {
                // 判断当前节点是否存在
                if (null == client.checkExists().forPath(keyPath)) {
                    client.create().creatingParentsIfNeeded().forPath(keyPath);
                    if (StringUtils.isNotBlank(defaultValue)) {
                        field.setAccessible(true);
                        field.set(bean, defaultValue);
                        field.setAccessible(false);
                    }
                    log.info("DCC 节点监听 创建节点: {}", keyPath);
                } else {
                    String configValue = new String(client.getData().forPath(keyPath));
                    if (StringUtils.isNotBlank(configValue)) {
                        field.setAccessible(true);
                        field.set(bean, configValue);
                        field.setAccessible(false);
                    }
                    log.info("DCC 节点监听 设置配置: {} {} {}", keyPath, field.getName(), configValue);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dccObjGroup.put(BASE_CONFIG_PATH_CONFIG.concat("/").concat(keyPath), bean);
        }
        return bean;
    }
}
