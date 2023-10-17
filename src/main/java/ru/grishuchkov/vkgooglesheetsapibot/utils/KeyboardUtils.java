package ru.grishuchkov.vkgooglesheetsapibot.utils;

import org.springframework.stereotype.Component;
import ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard.Button;
import ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard.ButtonAction;
import ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard.ButtonType;
import ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardUtils {

    public Keyboard getKeyboardForHomework() {
        List<List<Button>> buttons = getHomeWorkButtons();

        return Keyboard.builder()
                .oneTime(true)
                .buttons(buttons)
                .build();
    }

    private List<List<Button>> getHomeWorkButtons() {
        List<Button> firstButtonsLine = new ArrayList<Button>() {{
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW1\"}", "ДЗ 1")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW2\"}", "ДЗ 2")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW3\"}", "ДЗ 3")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW4\"}", "ДЗ 4")));
        }};
        List<Button> secondButtonsLine = new ArrayList<Button>() {{
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW5\"}", "ДЗ 5")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW6\"}", "ДЗ 6")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW7\"}", "ДЗ 7")));
            add(new Button("positive", new ButtonAction(ButtonType.text, "{\"button\": \"HW8\"}", "ДЗ 8")));
        }};

        List<List<Button>> buttons = new ArrayList<>() {{
            add(firstButtonsLine);
            add(secondButtonsLine);
        }};
        return buttons;
    }
}
