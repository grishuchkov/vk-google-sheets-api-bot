package ru.grishuchkov.vkgooglesheetsapibot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.grishuchkov.vkgooglesheetsapibot.dto.callback.NewMessageRequest;
import ru.grishuchkov.vkgooglesheetsapibot.utils.CallbackRequestMapper;

@Service
public class VkCallbackService implements CallbackService {

    private final CallbackRequestMapper mapper;
    public VkCallbackService(CallbackRequestMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @SneakyThrows
    public void handle(JsonNode json) {
        NewMessageRequest newMessageRequest = mapper.mapMessageNew(json);

    }
}
