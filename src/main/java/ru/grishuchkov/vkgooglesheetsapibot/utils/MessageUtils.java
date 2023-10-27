package ru.grishuchkov.vkgooglesheetsapibot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MessageUtils {

    private final ResourceBundle messagesResource;


    public boolean isMessageCommand(String messageText, String command) {
        return messageText.equalsIgnoreCase(messagesResource.getString(command));
    }

    public boolean isSubmitHomeworkMessage(String messageText, String submitHomeworkCommand) {
        boolean messageMatchesWithCommand = messageText.regionMatches
                (true, 0, messagesResource.getString(submitHomeworkCommand), 0, 10);

        boolean isCorrectLengthMessage = messageText.length() == 11 || messageText.length() == 12;

        return messageMatchesWithCommand && isCorrectLengthMessage;
    }

    public int extractHomeworkNumber(String messageText) {
        return Integer.parseInt(messageText.split("№")[1]);
    }

    public String prepareTelegramNotificationMessage(String studentName, String numberOfWork) {
        String messageTemplate = messagesResource.getString("telegram_notification_message");

        return String.format(messageTemplate, studentName, numberOfWork);
    }
}
