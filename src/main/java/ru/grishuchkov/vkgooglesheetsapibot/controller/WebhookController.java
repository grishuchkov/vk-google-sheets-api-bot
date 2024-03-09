package ru.grishuchkov.vkgooglesheetsapibot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentRequest;
import ru.grishuchkov.vkgooglesheetsapibot.dto.AcceptStudentResponse;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.AcceptStudentUseCase;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class WebhookController {

    private final AcceptStudentUseCase acceptUseCase;

    @PostMapping("accept-student")
    public ResponseEntity<AcceptStudentResponse> acceptStudent(@RequestBody AcceptStudentRequest request) {
        AcceptStudentResponse response = acceptUseCase.accept(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
