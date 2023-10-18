package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageEventRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;
import ru.grishuchkov.vkgooglesheetsapibot.utils.KeyboardUtil;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper callbackRequestMapper;
    private final ResourceBundle messagesResource;
    private final VkApiClient vkClient;
    private final KeyboardUtil keyboardUtil;

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
        if (typeOfRequest.equals("message_event")) {
            processMessageEvent(json);
        }
    }

    private void processMessage(JsonNode json) {
        MessageRequest request = callbackRequestMapper.mapMessageNew(json);

        String messageText = request.getMessageText();
        int userId = request.getUserId();
        int groupId = request.getGroupId();

        if(isSubmitHomeworkMessage(messageText, "submit_homework_command")){
            vkClient.sendMessage(groupId, userId, messagesResource.getString("homework_received"));
        }

        if (isMessageCommand(messageText, "homework_keyboard_command")) {
            Keyboard homeworkKeyboard = keyboardUtil.getHomeworkKeyboard();

            vkClient.sendMessage(groupId, userId, messagesResource.getString("homework_keyboard_received"), homeworkKeyboard);
        }
    }

    private void processMessageEvent(JsonNode jsonNode) {
        MessageEventRequest request = callbackRequestMapper.mapMessageEvent(jsonNode);

        int userId = request.getUserId();
        int groupId = request.getGroupId();
        int peerId = request.getPeerId();
        String eventId = request.getObjectEventId();

        vkClient.sendMessageEventAnswer(groupId, userId, peerId, eventId);
    }

    private boolean isMessageCommand(String messageText, String command) {
        return messageText.equalsIgnoreCase(messagesResource.getString(command));
    }

    private boolean isSubmitHomeworkMessage(String messageText, String submitHomeworkCommand){
       return messageText.regionMatches(true, 0,  messagesResource.getString(submitHomeworkCommand), 0, 10);
    }
}
