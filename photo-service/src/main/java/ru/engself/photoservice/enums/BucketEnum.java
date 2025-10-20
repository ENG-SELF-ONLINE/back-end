package ru.engself.photoservice.enums;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.engself.photoservice.config.BucketConfiguration;

@Getter
public enum BucketEnum {
    BOOKS,
    COVERS,
    PROFILE,
    DECKS,
    WORDS,
    EXPLANATIONS,
    AUDIOS;

    private String bucketName;

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Component
    public static class BucketEnumInitializer {

        @Autowired
        public BucketEnumInitializer(BucketConfiguration bucketConfiguration) {
            BOOKS.setBucketName(bucketConfiguration.getBooksBucket());
            COVERS.setBucketName(bucketConfiguration.getCoversBucket());
            PROFILE.setBucketName(bucketConfiguration.getProfileBucket());
            DECKS.setBucketName(bucketConfiguration.getDecksBucket());
            WORDS.setBucketName(bucketConfiguration.getWordsBucket());
            EXPLANATIONS.setBucketName(bucketConfiguration.getExplanationsBucket());
            AUDIOS.setBucketName(bucketConfiguration.getAudiosBucket());
        }
    }
}