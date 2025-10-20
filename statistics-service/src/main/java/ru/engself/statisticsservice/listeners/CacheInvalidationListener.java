package ru.engself.statisticsservice.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.engself.statisticsservice.services.RedisService;

@Component
@RequiredArgsConstructor
@DependsOn("kafkaConsumerConfig")
public class CacheInvalidationListener {

    private final RedisService redisService;

    @KafkaListener(topics = {
            "word-progress-updates",
            "reading-updates",
            "testing-updates",
            "activity-updates"
    }, groupId = "statisticsId")
    public void invalidateCache(String message) {
        redisService.invalidateCacheByPrefix(message);
    }
}