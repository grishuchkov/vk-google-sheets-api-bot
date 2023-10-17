package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.NewMessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;

@Service
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper mapper;
    private final VkApiClient vkApiClient;
    public VkCallbackService(CallbackRequestMapper mapper, VkApiClient vkApiClient) {
        this.mapper = mapper;
        this.vkApiClient = vkApiClient;
    }

    @Override
    public void handle(JsonNode json) {
        NewMessageRequest newMessageRequest = mapper.mapMessageNew(json);

        vkApiClient.sendMessage(newMessageRequest.getUserId(), newMessageRequest.getMessageText());
    }

    @Override
    public String getConfirmationCode(JsonNode json) {
        String groupId = json.get("group_id").asText();
        String confirmationCode = vkApiClient.getConfirmationCode(groupId);

        return confirmationCode;
    }
}
