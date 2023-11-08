package ru.grishuchkov.vkgooglesheetsapibot.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MessageUtils {

    private final ResourceBundle messagesResource;

    private String SUBMIT_HOMEWORK_COMMAND;
    private String START_COMMAND;

    @PostConstruct
    private void init() {
        SUBMIT_HOMEWORK_COMMAND = messagesResource.getString("submit_homework_command");
        START_COMMAND = messagesResource.getString("homework_keyboard_command");
    }

    public boolean isStarCommand(String messageText) {
        return messageText.equalsIgnoreCase(START_COMMAND);
    }

    public boolean isSubmitHomeworkCommand(String messageText) {

        boolean messageMatchesWithCommand = messageText.regionMatches
                (true, 0, SUBMIT_HOMEWORK_COMMAND, 0, 10);

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
