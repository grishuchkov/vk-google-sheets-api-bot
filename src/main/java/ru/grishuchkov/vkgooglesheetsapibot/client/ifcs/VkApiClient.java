package ru.grishuchkov.vkgooglesheetsapibot.client.ifcs;

import com.vk.api.sdk.objects.messages.Keyboard;
import ru.grishuchkov.vkgooglesheetsapibot.dto.VkMessage;

public interface VkApiClient {

    void sendMessage(Integer groupId, Integer userId, String text);

    void sendMessage(Integer groupId, Integer userId, String text, Keyboard keyboard);

    String getConfirmationCode(Integer groupId);

    void sendMessageEventAnswer(Integer groupId, Integer userId, Integer peerId, String eventId);

    String getUserScreenNameByUserId(int groupId, int userId);

    Integer getUserIdByScreenName(int groupId, String screenName);

    void sendMessage(VkMessage message);
}
