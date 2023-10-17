package ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Button {
    private String color;
    private ButtonAction action;
}
