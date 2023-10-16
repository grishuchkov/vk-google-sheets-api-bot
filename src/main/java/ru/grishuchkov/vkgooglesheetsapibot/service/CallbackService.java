package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface CallbackService {

    void handle(JsonNode json);
}
