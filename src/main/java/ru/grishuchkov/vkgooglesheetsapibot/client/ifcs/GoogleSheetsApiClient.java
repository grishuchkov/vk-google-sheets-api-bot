package ru.grishuchkov.vkgooglesheetsapibot.client.ifcs;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.SendHomeworkResponse;

public interface GoogleSheetsApiClient {
    SendHomeworkResponse sendHomework(Homework homework);
}
