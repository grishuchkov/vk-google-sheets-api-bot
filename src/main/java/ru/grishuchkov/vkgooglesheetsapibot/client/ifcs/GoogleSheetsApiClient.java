package ru.grishuchkov.vkgooglesheetsapibot.client.ifcs;

import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;

public interface GoogleSheetsApiClient {
    String sendHomework(Homework homework);
}
