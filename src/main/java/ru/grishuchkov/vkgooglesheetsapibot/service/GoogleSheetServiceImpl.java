package ru.grishuchkov.vkgooglesheetsapibot.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.GoogleSheetsApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.CheckNotificationRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.GoogleResponse;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.VkMessage;
import ru.grishuchkov.vkgooglesheetsapibot.enums.TypeOfCheck;
import ru.grishuchkov.vkgooglesheetsapibot.exception.GoogleClientException;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.GoogleSheetService;

import java.util.Optional;
import java.util.ResourceBundle;

@Service
@Log4j
@RequiredArgsConstructor
public class GoogleSheetServiceImpl implements GoogleSheetService {

    private final VkApiClient vkApiClient;
    private final GoogleSheetsApiClient sheetsApiClient;
    private final ResourceBundle messagesResource;

    private String TEXT_MESSAGE_WITHOUT_SCORE;
    private String TEXT_MESSAGE_WITH_SCORE;
    @PostConstruct
    private void init(){
        TEXT_MESSAGE_WITH_SCORE = messagesResource.getString("homework_check_notification_with_score");
        TEXT_MESSAGE_WITHOUT_SCORE = messagesResource.getString("homework_check_notification");
    }

    @Value("${vk.api.groupId}")
    private int courseGroupId;

    @Override
    public void processCheckNotification(CheckNotificationRequest notification) {

        Optional<String> text = prepareNotificationText(notification);

        if (text.isEmpty()) {
            return;
        }

        String messageText = text.get();
        int userId = getUserIdByScreenName(notification);

        VkMessage message = VkMessage.builder()
                .userId(userId)
                .groupId(courseGroupId)
                .text(messageText)
                .build();

        sendMessage(message);
    }

    private void sendMessage(VkMessage message) {
        vkApiClient.sendMessage(message);
    }

    private Integer getUserIdByScreenName(CheckNotificationRequest notification) {
        return vkApiClient.getUserIdByScreenName(courseGroupId, notification.getStudentScreenName());
    }

    private Optional<String> prepareNotificationText(CheckNotificationRequest notification) {
        Optional<String> text = Optional.empty();

        int numberOfWork = notification.getNumberOfWork();
        int score = notification.getScore();

        if (notification.getType() == TypeOfCheck.WITH_SCORE) {
            String message = String.format(TEXT_MESSAGE_WITH_SCORE, numberOfWork, score);
            text = Optional.of(message);
        }
        if (notification.getType() == TypeOfCheck.WITHOUT_SCORE) {
            String message = String.format(TEXT_MESSAGE_WITHOUT_SCORE, numberOfWork);
            text = Optional.of(message);
        }

        return text;
    }


    @Override
    public GoogleResponse sendHomework(Homework homework) {
        GoogleResponse googleResponse = new GoogleResponse();

        try {
            googleResponse = sheetsApiClient.sendHomework(homework);
        } catch (GoogleClientException ex) {
            log.error(ex);
        }

        return googleResponse;
    }
}

