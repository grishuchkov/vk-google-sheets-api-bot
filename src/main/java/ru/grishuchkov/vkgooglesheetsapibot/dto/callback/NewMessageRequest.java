package ru.grishuchkov.vkgooglesheetsapibot.dto.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageRequest extends BaseRequest {
    private String messageText;
}
