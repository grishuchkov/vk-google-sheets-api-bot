package ru.grishuchkov.vkgooglesheetsapibot.dto.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEventRequest extends BaseRequest{

    private int peerId;
    private String objectEventId;
}
