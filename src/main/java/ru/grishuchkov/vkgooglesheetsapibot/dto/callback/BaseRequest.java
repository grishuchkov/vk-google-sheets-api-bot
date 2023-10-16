package ru.grishuchkov.vkgooglesheetsapibot.dto.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseRequest {
    private long groupId;
    private String requestType;
    private String eventId;
    private String apiVersion;
    private String userId;
}
