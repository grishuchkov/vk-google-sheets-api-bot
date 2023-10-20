package ru.grishuchkov.vkgooglesheetsapibot.service.ifcs;

import ru.grishuchkov.vkgooglesheetsapibot.dto.CheckNotification;

public interface GoogleSheetService {

    void processCheckNotification(CheckNotification notification);
}
