package ru.engself.trackerservice.exceptions;

public class ActivityNotFoundException extends RuntimeException {

    public ActivityNotFoundException() {
        super();
    }

    public ActivityNotFoundException(String message) {
        super(message);
    }

    public ActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ActivityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
