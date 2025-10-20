package ru.engself.statisticsservice.services;

public interface RedisService {

    Object getCachedStats(String key);

    void cacheStats(String key, Object stats);

    void invalidateCacheByPrefix(String keyPrefix);

}
