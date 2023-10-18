package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.vk.api.sdk.objects.messages.Keyboard;

public interface VkApiClient {

    void sendMessage(Integer groupId, Integer userId, String text);

    void sendMessage(Integer groupId, Integer userId, String text, Keyboard keyboard);

    String getConfirmationCode(Integer groupId);

    void sendMessageEventAnswer(Integer groupId, Integer userId, Integer peerId, String eventId);
}
