package ru.grishuchkov.vkgooglesheetsapibot.exception;

public class JsonParseFiledException extends RuntimeException{
    public JsonParseFiledException() {
        super();
    }

    public JsonParseFiledException(String message) {
        super(message);
    }
}
