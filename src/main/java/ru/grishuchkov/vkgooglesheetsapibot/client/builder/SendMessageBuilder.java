package ru.grishuchkov.vkgooglesheetsapibot.client.builder;

import org.springframework.web.util.UriComponentsBuilder;

public class SendMessageBuilder {

    private String userId;
    private String message;
    private String keyboard;

    public SendMessageBuilder(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public SendMessageBuilder withKeyboard(String keyboard) {
        this.keyboard = keyboard;
        return this;
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