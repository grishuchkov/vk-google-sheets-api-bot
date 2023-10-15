package ru.grishuchkov.vkgooglesheetsapibot;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VkCallbackController {

    @GetMapping("/api/v1/callback")
    public ResponseEntity<String> getCallback(String callbackJson) {
        
        return ResponseEntity.status(200).body("ok");
    }
}
