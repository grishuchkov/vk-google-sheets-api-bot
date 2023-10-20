package ru.grishuchkov.vkgooglesheetsapibot.dto;

import lombok.Getter;
import lombok.Setter;
import ru.grishuchkov.vkgooglesheetsapibot.enums.TypeOfCheck;

@Getter
@Setter
public class CheckNotification {
    private String studentScreenName;
    private int numberOfWork;
    private TypeOfCheck type;
    private int score;
}
