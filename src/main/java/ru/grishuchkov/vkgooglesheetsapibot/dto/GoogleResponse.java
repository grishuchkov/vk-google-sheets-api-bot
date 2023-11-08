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
    private String studentFileUrl;

    public boolean hasValidStatusCode(){
        return statusCode == HttpStatus.SC_OK;
    }

    public boolean hasBadStatusCode(){
        return statusCode == HttpStatus.SC_BAD_REQUEST;
    }

    public boolean hasConflictStatusCode(){
        return statusCode == HttpStatus.SC_CONFLICT;
    }

    public boolean hasTelegramChatId(){
        return !telegramChatId.isEmpty();
    }
}
