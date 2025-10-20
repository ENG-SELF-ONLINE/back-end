package ru.engself.photoservice.exceptions;

public class ImageDownloadException extends RuntimeException {

    public ImageDownloadException(String message) {
        super(message);
    }

}