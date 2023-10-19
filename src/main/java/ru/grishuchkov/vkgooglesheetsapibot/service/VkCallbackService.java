package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.GoogleSheetsApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageEventRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;
import ru.grishuchkov.vkgooglesheetsapibot.utils.KeyboardUtil;
import ru.grishuchkov.vkgooglesheetsapibot.utils.MessageUtils;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class VkCallbackService implements CallbackService {

    private final ResourceBundle messagesResource;

    private final VkApiClient vkClient;
    private final GoogleSheetsApiClient sheetsApiClient;

    private final KeyboardUtil keyboardUtil;
    private final MessageUtils messageUtils;
    private final CallbackRequestMapper callbackRequestMapper;

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

        if(messageUtils.isSubmitHomeworkMessage(messageText, "submit_homework_command")){
            int homeworkNumber = messageUtils.extractHomeworkNumber(messageText);
            processSubmitHomework(groupId, new Homework(userId, homeworkNumber));
        }

        if (messageUtils.isMessageCommand(messageText, "homework_keyboard_command")) {
            Keyboard homeworkKeyboard = keyboardUtil.getHomeworkKeyboard();
            vkClient.sendMessage(groupId, userId,
                    messagesResource.getString("homework_keyboard_received"), homeworkKeyboard);
        }
    }

    @SneakyThrows
    private void processSubmitHomework(int groupId, Homework homework){
        vkClient.sendMessage(groupId, homework.getUserId(), messagesResource.getString("homework_received"));

        String screenName = vkClient.getUserScreenNameByUserId(groupId, homework.getUserId());
        homework.setScreenName(screenName);

        sheetsApiClient.sendHomework(homework);

        vkClient.sendMessage(groupId, homework.getUserId(), "Дз отправили!");
    }

    private void processMessageEvent(JsonNode jsonNode) {
        MessageEventRequest request = callbackRequestMapper.mapMessageEvent(jsonNode);

        int userId = request.getUserId();
        int groupId = request.getGroupId();
        int peerId = request.getPeerId();
        String eventId = request.getObjectEventId();

        vkClient.sendMessageEventAnswer(groupId, userId, peerId, eventId);
    }
}
