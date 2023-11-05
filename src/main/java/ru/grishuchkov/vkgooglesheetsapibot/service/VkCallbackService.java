package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.GoogleResponse;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.VkMessage;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageEventRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.MessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.CallbackService;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.GoogleSheetService;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.TelegramBotService;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;
import ru.grishuchkov.vkgooglesheetsapibot.utils.KeyboardUtil;
import ru.grishuchkov.vkgooglesheetsapibot.utils.MessageUtils;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
@Log4j
public class VkCallbackService implements CallbackService {

    private final ResourceBundle messagesResource;
    private final TelegramBotService telegram;
    private final VkApiClient vkClient;
    private final GoogleSheetService googleSheetService;
    private final KeyboardUtil keyboardUtil;
    private final MessageUtils messageUtils;
    private final CallbackRequestMapper callbackRequestMapper;

    private final String HOMEWORK_ATTEMPT_TEXT = messagesResource.getString("homework_attempt_to_send");
    private final String FAIL_MESSAGE = messagesResource.getString("homework_sending_error");
    private final String SUCCESS_MESSAGE = messagesResource.getString("homework_successfully_sent");

    @Override
    public String getConfirmationCode(JsonNode json) {
        int groupId = json.get("group_id").asInt();

        return vkClient.getConfirmationCode(groupId);
    }

    @Override
    public void handle(JsonNode json) {
        String typeOfRequest = json.get("type").asText();

        if (typeOfRequest.equals("message_new")) {
            processNewMessage(json);
        }
        if (typeOfRequest.equals("message_event")) {
            processMessageEvent(json);
        }
    }

    private void processNewMessage(JsonNode json) {
        MessageRequest newMessageRequest = callbackRequestMapper.mapMessageNew(json);

        VkMessage message = VkMessage.builder()
                .text(newMessageRequest.getMessageText())
                .userId(newMessageRequest.getUserId())
                .groupId(newMessageRequest.getGroupId())
                .build();

        if (messageUtils.isStarCommand(message.getText())) {
            prepareAndSendResponseToStartCommand(message);
        }

        if (messageUtils.isSubmitHomeworkCommand(message.getText())) {
            processSubmitHomeworkCommand(message);
        }
    }

    private void processSubmitHomeworkCommand(VkMessage message) {
        sendMessageToUser(message, HOMEWORK_ATTEMPT_TEXT);

        Homework homework = prepareHomeworkFromMessage(message);
        GoogleResponse googleResponse = googleSheetService.sendHomework(homework);

        if (!googleResponse.hasValidStatusCode()) {
            sendMessageToUser(message, FAIL_MESSAGE);
            log.error("Google returned bad status code at sendHomework()");
            return;
        }

        sendMessageToUser(message, SUCCESS_MESSAGE);

        if (googleResponse.hasTelegramChatId()) {
            prepareAndSendTelegramNotification(googleResponse);
        }
    }

    private void prepareAndSendTelegramNotification(GoogleResponse response) {
        telegram.processHomeworkNotificationMessage(response.getTelegramChatId(),
                response.getStudentName(),
                response.getNumberOfWork());
    }

    private Homework prepareHomeworkFromMessage(VkMessage message) {
        Integer userId = message.getUserId();
        Integer groupId = message.getGroupId();

        Integer homeworkNumber = messageUtils.extractHomeworkNumber(message.getText());
        String userScreenName = vkClient.getUserScreenNameByUserId(groupId, userId);

        return Homework.builder()
                .userId(userId)
                .screenName(userScreenName)
                .numberOfHomework(homeworkNumber)
                .build();
    }

    private void sendMessageToUser(VkMessage message, String text) {
        message.setText(text);
        vkClient.sendMessage(message);
    }

    private void prepareAndSendResponseToStartCommand(VkMessage message) {
        String messageText = messagesResource.getString("homework_keyboard_received");
        Keyboard homeworkKeyboard = keyboardUtil.getHomeworkKeyboard();

        message.setKeyboard(homeworkKeyboard);
        message.setText(messageText);

        vkClient.sendMessage(message);
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
