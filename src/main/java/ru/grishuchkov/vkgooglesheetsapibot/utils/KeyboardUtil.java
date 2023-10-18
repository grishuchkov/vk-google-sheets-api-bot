package ru.grishuchkov.vkgooglesheetsapibot.utils;

import com.vk.api.sdk.objects.messages.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardUtil {

    public Keyboard getHomeworkKeyboard() {

        return new Keyboard().setButtons(getButtonForHomeworkKeyboard(4,2, 8))
                .setOneTime(true);
    }

    private List<List<KeyboardButton>> getButtonForHomeworkKeyboard(int rows, int columns, int num) {
        int numberOfWork = 1;
        List<List<KeyboardButton>> buttons = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            List<KeyboardButton> line = new ArrayList<>();

            for (int j = 0; j < columns && numberOfWork <= num; j++) {
                KeyboardButton button = new KeyboardButton()
                        .setAction(new KeyboardButtonAction()
                                .setLabel("Сдать ДЗ " + numberOfWork)
                                .setPayload("{\"button\": \"" + numberOfWork + "\"}")
                                .setType(TemplateActionTypeNames.TEXT));
                button.setColor(KeyboardButtonColor.POSITIVE);
                line.add(button);
                numberOfWork += 1;
            }

            buttons.add(line);
        }

        return buttons;
    }
}
