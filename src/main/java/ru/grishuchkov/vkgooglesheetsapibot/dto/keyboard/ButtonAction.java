package ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ButtonAction {
    private ButtonType type;
    private String payload;
    private String label;
}
