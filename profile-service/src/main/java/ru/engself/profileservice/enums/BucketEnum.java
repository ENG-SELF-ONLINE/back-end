package ru.engself.profileservice.enums;

import lombok.Getter;

@Getter
public enum BucketEnum {
    BOOKS("books"),
    COVERS("covers"),
    PROFILE("profile"),
    DECKS("decks"),
    WORDS("words"),
    EXPLANATIONS("explanations"),
    AUDIOS("audios");

    private final String bucketName;

    BucketEnum(String bucketName) {
        this.bucketName = bucketName;
    }
}