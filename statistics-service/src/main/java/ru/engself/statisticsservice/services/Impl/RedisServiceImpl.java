package ru.engself.statisticsservice.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.engself.statisticsservice.services.RedisService;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.cache.ttl}")
    private long cacheTtl;

    @Override
    public Object getCachedStats(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void cacheStats(String key, Object stats) {
        redisTemplate.opsForValue().set(key, stats, cacheTtl, TimeUnit.HOURS);
    }

    @Override
    public void invalidateCacheByPrefix(String keyPrefix) {
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}