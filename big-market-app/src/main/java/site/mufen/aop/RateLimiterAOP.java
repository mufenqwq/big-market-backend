package site.mufen.aop;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.mufen.types.annotations.DCCValue;

import java.util.concurrent.TimeUnit;

/**
 * @author mufen
 * @Description 限流切面
 * @create 2024/12/5 16:32
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAOP {

    @DCCValue("rateLimiterSwitch:close")
    private String rateLimiterSwitch;

    // 个人限频记录1分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES).build();

    // 个人限额黑名单24h - 分布式业务场景 可以记录到 Redis中
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder()
        .expireAfterWrite(24, TimeUnit.HOURS).build();

    @Pointcut("@@annotation(site.mufen.types.annotations.Rate)")
    public void aopPoint() {

    }
}
