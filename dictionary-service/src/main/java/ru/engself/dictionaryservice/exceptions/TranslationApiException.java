package ru.engself.dictionaryservice.exceptions;

public class TranslationApiException extends RuntimeException {

    public TranslationApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
