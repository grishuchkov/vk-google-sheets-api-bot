package ru.grishuchkov.vkgooglesheetsapibot.dto.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequest {
    private int groupId;
    private int userId;
    private String requestType;
    private String eventId;
    private String apiVersion;
}
