package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.vk.api.sdk.objects.messages.Keyboard;

public interface VkApiClient {

    void sendMessage(Integer groupId, String text, Integer userId);
    void sendMessage(Integer groupId, String text, Integer userId, Keyboard keyboard);
    String getConfirmationCode(Integer groupId);
}
