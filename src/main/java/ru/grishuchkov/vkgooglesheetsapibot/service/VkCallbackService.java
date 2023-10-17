package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard.Keyboard;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;
import ru.grishuchkov.vkgooglesheetsapibot.utils.KeyboardUtils;

import java.util.ResourceBundle;

@Service
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper callbackRequestMapper;
    private final ResourceBundle messagesResource;
    private final VkApiClient vkApiClient;

    private final KeyboardUtils keyboardUtils;

    public VkCallbackService(CallbackRequestMapper callbackRequestMapper, ResourceBundle messagesResource, VkApiClient vkApiClient, KeyboardUtils keyboardUtils) {
        this.callbackRequestMapper = callbackRequestMapper;
        this.messagesResource = messagesResource;
        this.vkApiClient = vkApiClient;
        this.keyboardUtils = keyboardUtils;
    }

    @Override
    public void handle(JsonNode json) {
        if (json.get("type").asText().equals("message_new")) {
            processMessage(json);
        }
    }

    @Override
    public String getConfirmationCode(JsonNode json) {
        String groupId = json.get("group_id").asText();

        return vkApiClient.getConfirmationCode(groupId);
    }

    @SneakyThrows
    private void processMessage(JsonNode json) {
        MessageRequest request = callbackRequestMapper.mapMessageNew(json);

        String messageText = request.getMessageText();
        String userId = request.getUserId();

        if (messageText.equalsIgnoreCase(messagesResource.getString("homework_command"))) {
            vkApiClient.sendMessage(userId, messagesResource.getString("homework_received"), null);
        }

        if (messageText.equalsIgnoreCase(messagesResource.getString("keyboard_command"))) {
            Keyboard keyboardObject = keyboardUtils.getKeyboardForHomework();
            String keyboardJson = new ObjectMapper().writeValueAsString(keyboardObject);

            vkApiClient.sendMessage(userId, messagesResource.getString("keyboard_received"), keyboardJson);
        }

    }


}
