package com.qijiejin.studentinfo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 默认使用 Caffeine 做进程内缓存，模拟简历中 Redis 的"高频查询缓存"诉求。
 * 切换 Redis：
 *   1) pom.xml 加入 spring-boot-starter-data-redis
 *   2) 把下方 CaffeineCacheManager 替换为 RedisCacheManager.builder(connectionFactory).build()
 *   3) application.yml 加 spring.data.redis.host / port
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("student");
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
        );
        return manager;
    }
}
