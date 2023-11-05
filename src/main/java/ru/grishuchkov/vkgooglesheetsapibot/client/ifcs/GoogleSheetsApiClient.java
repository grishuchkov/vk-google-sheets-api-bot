package ru.grishuchkov.vkgooglesheetsapibot.client.ifcs;

import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.GoogleResponse;

public interface GoogleSheetsApiClient {
    GoogleResponse sendHomework(Homework homework);
}
