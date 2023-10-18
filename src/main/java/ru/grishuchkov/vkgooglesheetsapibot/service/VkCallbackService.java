package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper callbackRequestMapper;
    private final ResourceBundle messagesResource;
    private final VkApiClient vkClient;
    @Override
    public String getConfirmationCode(JsonNode json) {
        int groupId = json.get("group_id").asInt();

        return vkClient.getConfirmationCode(groupId);
    }

    @Override
    public void handle(JsonNode json) {
        String typeOfRequest = json.get("type").asText();

        if (typeOfRequest.equals("message_new")) {
            processMessage(json);
        }
    }

    @SneakyThrows
    private void processMessage(JsonNode json) {
        MessageRequest request = callbackRequestMapper.mapMessageNew(json);

        String messageText = request.getMessageText();
        int userId = request.getUserId();
        int groupId = request.getGroupId();

        if (isCommand(messageText, "homework_keyboard_command")) {
            vkClient.sendMessage(groupId, messagesResource.getString("homework_received"), userId);
        }
    }

    private boolean isCommand(String messageText, String command) {
        return messageText.equalsIgnoreCase(messagesResource.getString(command));
    }
}
