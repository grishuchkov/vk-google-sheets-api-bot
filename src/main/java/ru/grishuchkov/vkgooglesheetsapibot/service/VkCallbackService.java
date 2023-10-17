package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;

import java.util.ResourceBundle;

@Service
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper mapper;
    private final ResourceBundle messagesResource;
    private final VkApiClient vkApiClient;

    public VkCallbackService(CallbackRequestMapper mapper, ResourceBundle messagesResource, VkApiClient vkApiClient) {
        this.mapper = mapper;
        this.messagesResource = messagesResource;
        this.vkApiClient = vkApiClient;
    }

    @Override
    public void handle(JsonNode json) {
        if (json.get("type").asText().equals("message_new")) {
            processMessage(json);
            return;
        }
    }

    @Override
    public String getConfirmationCode(JsonNode json) {
        String groupId = json.get("group_id").asText();

        return vkApiClient.getConfirmationCode(groupId);
    }

    private void processMessage(JsonNode json) {
        MessageRequest request = mapper.mapMessageNew(json);
        sendMessageByTextCondition(request, "homework_command", "homework_received");
    }

    private void sendMessageByTextCondition(MessageRequest messageRequest, String conditionTextKey, String messageTextKey) {
        String messageText = messageRequest.getMessageText();
        String userId = messageRequest.getUserId();

        if (messageText.equalsIgnoreCase(messagesResource.getString(conditionTextKey))) {
            vkApiClient.sendMessage(userId, messagesResource.getString(messageTextKey));
        }
    }
}
