package ru.grishuchkov.vkgooglesheetsapibot.dto.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest extends BaseRequest {
    private String messageText;
}
