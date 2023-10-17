package ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard;

import lombok.Getter;

@Getter
public enum ButtonType {

    TEXT("text"),
    CALLBACK("callback");

    private final String type;
    ButtonType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}