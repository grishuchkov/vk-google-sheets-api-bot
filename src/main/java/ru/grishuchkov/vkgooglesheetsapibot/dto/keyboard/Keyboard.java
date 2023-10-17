package ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard;

import lombok.Builder;

import java.util.List;

@Builder
public class Keyboard {
    private Boolean oneTime;
    private List<List<Button>> buttons;
}
