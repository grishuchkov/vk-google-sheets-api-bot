package ru.grishuchkov.vkgooglesheetsapibot.utils;

import com.vk.api.sdk.objects.messages.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardUtil {

    @Value("${homeworks.count}")
    private int countOfHomework;

    public Keyboard getHomeworkKeyboard() {
        return new Keyboard()
                .setButtons(getButtonForHomeworkKeyboard(3, countOfHomework))
                .setOneTime(true);
    }

    private List<List<KeyboardButton>> getButtonForHomeworkKeyboard(int columns, int num) {

        int rows = rowsCalculate(num, columns);

        int numberOfWork = 1;
        List<List<KeyboardButton>> buttons = new ArrayList<>();

        for (int i = 1; i <= rows; i++) {
            List<KeyboardButton> line = new ArrayList<>();

            for (int j = 0; j < columns && numberOfWork <= num; j++) {

                KeyboardButton button = new KeyboardButton()
                        .setAction(new KeyboardButtonAction()
                                .setLabel("Сдать ДЗ №" + numberOfWork)
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

    private int rowsCalculate(int countOfButtons, int countOfColumns) {
        return (int) (Math.ceil((double) countOfButtons / countOfColumns));
    }
}
