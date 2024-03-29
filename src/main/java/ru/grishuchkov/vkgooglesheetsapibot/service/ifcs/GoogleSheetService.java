package ru.grishuchkov.vkgooglesheetsapibot.service.ifcs;

import ru.grishuchkov.vkgooglesheetsapibot.dto.CheckNotificationRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.GoogleResponse;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;

public interface GoogleSheetService {
    GoogleResponse sendHomework(Homework homework);

    void processCheckNotification(CheckNotificationRequest notification);
}
