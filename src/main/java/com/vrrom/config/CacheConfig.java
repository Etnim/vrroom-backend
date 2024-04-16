package com.vrrom.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS);
    }

    @Bean
    public CaffeineCacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("makesCache", "modelsCache", "euriborRates");
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(true);
        cacheManager.setAsyncCacheMode(true);
        return cacheManager;
    }
}
