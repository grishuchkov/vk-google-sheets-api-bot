package ru.grishuchkov.vkgooglesheetsapibot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.grishuchkov.vkgooglesheetsapibot.enums.CallbackType;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.CallbackService;

@RestController
public class VkCallbackController {
    private final CallbackService callbackService;
    private final ThreadPoolTaskExecutor executor;

    @Autowired
    public VkCallbackController(CallbackService callbackService, @Qualifier("taskExecutor") ThreadPoolTaskExecutor executor) {
        this.callbackService = callbackService;
        this.executor = executor;
    }

    @PostMapping("/api/v1/callback")
    public ResponseEntity<String> getCallback(@RequestBody JsonNode callbackJson) {
        String type = callbackJson.get("type").asText();

        if (type.equals(CallbackType.CONFIRMATION.getType())) {
            String confirmationCode = callbackService.getConfirmationCode(callbackJson);
            return new ResponseEntity<>(confirmationCode, HttpStatusCode.valueOf(200));
        }

        executor.execute(() -> callbackService.handle(callbackJson));

        return new ResponseEntity<>("ok", HttpStatusCode.valueOf(200));
    }
}
