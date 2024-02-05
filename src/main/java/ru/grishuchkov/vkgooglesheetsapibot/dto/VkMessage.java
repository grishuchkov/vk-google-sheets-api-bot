package ru.grishuchkov.vkgooglesheetsapibot.dto;

import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VkMessage {
    private Integer groupId;
    private Integer userId;
    private String text;
    private Keyboard keyboard;

    public boolean hasKeyboard(){
        return this.keyboard != null;
    }
}
