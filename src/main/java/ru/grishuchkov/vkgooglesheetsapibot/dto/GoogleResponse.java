package ru.grishuchkov.vkgooglesheetsapibot.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

@Getter
@Setter
public class GoogleResponse {
    private int statusCode;
    private String telegramChatId;
    private String studentName;
    private String numberOfWork;

    public boolean hasValidStatusCode(){
        return statusCode == HttpStatus.SC_OK;
    }

    public boolean hasTelegramChatId(){
        return !telegramChatId.isEmpty();
    }
}
