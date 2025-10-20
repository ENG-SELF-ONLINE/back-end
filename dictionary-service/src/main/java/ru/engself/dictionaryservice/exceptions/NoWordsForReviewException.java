package ru.engself.dictionaryservice.exceptions;

public class NoWordsForReviewException extends RuntimeException {
    public NoWordsForReviewException(String message) {
        super(message);
    }
}
