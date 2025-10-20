package ru.engself.photoservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BucketConfiguration {

    @Value("${minio.buckets.books}")
    private String booksBucket;

    @Value("${minio.buckets.covers}")
    private String coversBucket;

    @Value("${minio.buckets.profile}")
    private String profileBucket;

    @Value("${minio.buckets.decks}")
    private String decksBucket;

    @Value("${minio.buckets.words}")
    private String wordsBucket;

    @Value("${minio.buckets.explanations}")
    private String explanationsBucket;

    @Value("${minio.buckets.audios}")
    private String audiosBucket;
}