package ru.grishuchkov.vkgooglesheetsapibot.exception;

public class VkClientException extends RuntimeException{
    public VkClientException() {
        super();
    }

    public VkClientException(String message) {
        super(message);
    }

    public VkClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public VkClientException(Throwable cause) {
        super(cause);
    }
}
