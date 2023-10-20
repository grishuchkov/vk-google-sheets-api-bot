package ru.grishuchkov.vkgooglesheetsapibot.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.grishuchkov.vkgooglesheetsapibot.dto.CheckNotification;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.GoogleSheetService;

@RestController
public class GoogleSheetController {
    private final ThreadPoolTaskExecutor executor;
    private final GoogleSheetService googleSheetService;
    public GoogleSheetController(@Qualifier("taskExecutor") ThreadPoolTaskExecutor executor, GoogleSheetService googleSheetService) {
        this.executor = executor;
        this.googleSheetService = googleSheetService;
    }

    @PostMapping("/api/v1/check-notification")
    public ResponseEntity<String> getCheckNotification(@RequestBody CheckNotification notification){

        executor.execute(() -> googleSheetService.processCheckNotification(notification));

        return ResponseEntity.ok("ok");
    }
}
