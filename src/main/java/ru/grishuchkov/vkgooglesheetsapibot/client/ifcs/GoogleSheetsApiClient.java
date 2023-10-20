package ru.grishuchkov.vkgooglesheetsapibot.client.ifcs;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;

public interface GoogleSheetsApiClient {
    String sendHomework(Homework homework) throws JsonProcessingException;
}
