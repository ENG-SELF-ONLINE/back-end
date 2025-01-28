package ru.engself.dictionaryservice.exceptions;

public class DeckNotFoundException extends RuntimeException {

    public DeckNotFoundException() {
        super();
    }

    public DeckNotFoundException(String message) {
        super(message);
    }

    public DeckNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeckNotFoundException(Throwable cause) {
        super(cause);
    }

    protected DeckNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
