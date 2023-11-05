package ru.grishuchkov.vkgooglesheetsapibot.dto;

import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VkMessage {
    private Integer groupId;
    private Integer userId;
    private String text;
    private Keyboard keyboard;

    public boolean hasKeyboard(){
        return this.keyboard != null;
    }
}
