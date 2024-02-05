package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vk.api.sdk.objects.messages.Keyboard;
import jakarta.annotation.PostConstruct;
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

    private String HOMEWORK_ATTEMPT_TEXT;
    private String FAIL_TEXT;
    private String SUCCESS_TEXT;
    private String DEADLINE_EXPIRED_TEXT;

    @PostConstruct
    private void init() {
        HOMEWORK_ATTEMPT_TEXT = messagesResource.getString("homework_attempt_to_send");
        FAIL_TEXT = messagesResource.getString("homework_sending_error");
        SUCCESS_TEXT = messagesResource.getString("homework_successfully_sent");
        DEADLINE_EXPIRED_TEXT = messagesResource.getString("homework_deadline_expired");
    }

    @Override
    public String getConfirmationCode(JsonNode requestJson) {
        int groupId = requestJson.get("group_id").asInt();

        log.debug("getConfirmationCode() in service");
        return vkClient.getConfirmationCode(groupId);
    }

    @Override
    public void handle(JsonNode requestJson) {
        String typeOfRequest = requestJson.get("type").asText();

        if (typeOfRequest.equals("message_new")) {
            processNewMessage(requestJson);
        }
        if (typeOfRequest.equals("message_event")) {
            processMessageEvent(requestJson);
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
            log.debug("Bot's starting command was called");
            prepareAndSendResponseToStartCommand(message);
        }

        if (messageUtils.isSubmitHomeworkCommand(message.getText())) {
            log.debug("Bot's submit homework command was called");
            processSubmitHomeworkCommand(message);
        }
    }

    private void prepareAndSendResponseToStartCommand(VkMessage message) {
        String messageText = messagesResource.getString("homework_keyboard_received");
        Keyboard homeworkKeyboard = keyboardUtil.getHomeworkKeyboard();

        message.setKeyboard(homeworkKeyboard);
        message.setText(messageText);

        vkClient.sendMessage(message);
    }

    private void processSubmitHomeworkCommand(VkMessage message) {
        Homework homework = prepareHomeworkFromMessage(message);
        sendMessageToUser(message, HOMEWORK_ATTEMPT_TEXT);

        GoogleResponse googleResponse = googleSheetService.sendHomework(homework);

        if (googleResponse.hasBadStatusCode()) {
            log.error("Google returned bad status code at sendHomework()");
            sendMessageToUser(message, FAIL_TEXT);
            return;
        }

        if (googleResponse.hasConflictStatusCode()) {
            log.debug("Deadline expired");
            sendMessageToUser(message, DEADLINE_EXPIRED_TEXT);
            return;
        }

        sendMessageToUser(message, SUCCESS_TEXT);

        if (googleResponse.hasTelegramChatId()) {
            prepareAndSendTelegramNotification(googleResponse);
        }
    }

    private void sendMessageToUser(VkMessage message, String text) {
        message.setText(text);
        vkClient.sendMessage(message);
    }

    private Homework prepareHomeworkFromMessage(VkMessage message) {
        Integer userId = message.getUserId();
        Integer groupId = message.getGroupId();

        int homeworkNumber = messageUtils.extractHomeworkNumber(message.getText());
        String userScreenName = vkClient.getUserScreenNameByUserId(groupId, userId);

        return Homework.builder()
                .userId(userId)
                .screenName(userScreenName)
                .numberOfHomework(homeworkNumber)
                .build();
    }

    private void prepareAndSendTelegramNotification(GoogleResponse response) {
        String chatId = response.getTelegramChatId();
        String text = prepareMessageTextFromGoogleResponse(response);

        telegram.sendMessage(chatId, text);
    }

    private String prepareMessageTextFromGoogleResponse(GoogleResponse response) {
        String studentName = response.getStudentName();
        String numberOfWork = response.getNumberOfWork();
        String fileUrl = response.getStudentFileUrl();

        return messageUtils.prepareTelegramNotificationMessage(studentName, numberOfWork, fileUrl);
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
