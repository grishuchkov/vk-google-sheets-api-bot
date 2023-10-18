package ru.grishuchkov.vkgooglesheetsapibot.client.builder;

import org.springframework.web.util.UriComponentsBuilder;

public class SendMessageBuilder {

    private final String userId;
    private final String message;
    private String keyboard;

    public SendMessageBuilder(String userId, String message, String keyboard) {
        this.userId = userId;
        this.message = message;
        this.keyboard = keyboard;
    }

    public UriComponentsBuilder returnUri(UriComponentsBuilder baseUri) {
        if (keyboard == null) {
            keyboard = "";
        }

        return baseUri
                .queryParam("message", message)
                .queryParam("user_id", userId)
                .queryParam("keyboard", keyboard);
    }
}