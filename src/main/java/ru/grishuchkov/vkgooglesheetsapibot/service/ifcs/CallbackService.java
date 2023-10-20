package ru.grishuchkov.vkgooglesheetsapibot.service.ifcs;

import com.fasterxml.jackson.databind.JsonNode;

public interface CallbackService {

    void handle(JsonNode json);

    String getConfirmationCode(JsonNode json);
}
