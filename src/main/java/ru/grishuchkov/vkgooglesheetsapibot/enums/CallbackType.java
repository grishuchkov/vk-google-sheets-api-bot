package ru.grishuchkov.vkgooglesheetsapibot.enums;

import lombok.Getter;

@Getter
public enum CallbackType {

    CONFIRMATION("confirmation"),
    MESSAGE_EVENT("message_event"),
    MESSAGE_NEW("message_new");

    CallbackType(String type) {
        this.type = type;
    }

    private final String type;


}
