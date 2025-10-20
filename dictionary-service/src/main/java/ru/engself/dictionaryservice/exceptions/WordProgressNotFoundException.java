package ru.engself.dictionaryservice.exceptions;

public class WordProgressNotFoundException extends RuntimeException {

    public WordProgressNotFoundException() {
        super();
    }

    public WordProgressNotFoundException(String message) {
        super(message);
    }

    public WordProgressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordProgressNotFoundException(Throwable cause) {
        super(cause);
    }

    protected WordProgressNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
