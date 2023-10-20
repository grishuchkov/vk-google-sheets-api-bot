package ru.grishuchkov.vkgooglesheetsapibot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.VkApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.CheckNotification;
import ru.grishuchkov.vkgooglesheetsapibot.enums.TypeOfCheck;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.GoogleSheetService;

import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class GoogleSheetServiceImpl implements GoogleSheetService {

    private final VkApiClient vkApiClient;
    private final ResourceBundle messagesResource;

    @Value("${vkGroupIdForCourse}")
    private int courseGroupId;

    @Override
    public void processCheckNotification(CheckNotification notification) {
        int userId = vkApiClient.getUserIdByScreenName(courseGroupId, notification.getStudentScreenName());

        if (notification.getType() == TypeOfCheck.WITHOUT_SCORE) {
            String messageTemplate = messagesResource.getString("homework_check_notification");
            String message = String.format(messageTemplate, notification.getNumberOfWork());

            vkApiClient.sendMessage(courseGroupId, userId, message);
            return;
        }

        if (notification.getType() == TypeOfCheck.WITH_SCORE){
            String messageTemplate = messagesResource.getString("homework_check_notification_with_score");
            String message = String.format(messageTemplate, notification.getNumberOfWork(), notification.getScore());

            vkApiClient.sendMessage(courseGroupId, userId, message);
            return;
        }

    }
}
