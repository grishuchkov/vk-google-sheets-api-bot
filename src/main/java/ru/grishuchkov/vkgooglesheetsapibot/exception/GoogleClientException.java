package ru.grishuchkov.vkgooglesheetsapibot.exception;

public class GoogleClientException extends RuntimeException{
    public GoogleClientException() {
        super();
    }

    public GoogleClientException(String message) {
        super(message);
    }

    public GoogleClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleClientException(Throwable cause) {
        super(cause);
    }
}
