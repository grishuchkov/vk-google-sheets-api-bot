package ru.grishuchkov.vkgooglesheetsapibot.exception;

public class BadStatusCodeException extends RuntimeException{

    public BadStatusCodeException(String message) {
        super(message);
    }
}
