package ru.grishuchkov.vkgooglesheetsapibot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendHomeworkResponse {
    private int statusCode;
    private String numberOfWork;
    private String telegramChatId;
}
