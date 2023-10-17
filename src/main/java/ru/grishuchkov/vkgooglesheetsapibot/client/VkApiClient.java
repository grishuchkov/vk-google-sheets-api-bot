package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.grishuchkov.vkgooglesheetsapibot.client.builder.SendMessageBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Random;

@Service

public class VkApiClient {
    private final RestTemplate restTemplate;
    private final Random random;

    public VkApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.random = new Random();
    }

    @Value("${apiBaseUrl}")
    private String vkApiUrl;

    @Value("${apiToken}")
    private String token;

    @Value("${apiVersion}")
    private String apiVersion;


    public void sendMessage(String userId, String text, String keyboard) {
        SendMessageBuilder messageBuilder = new SendMessageBuilder(userId, text);
        messageBuilder.withKeyboard(keyboard);
        URI sendMessageUri = messageBuilder
                                .returnUri(getPreparedBaseUrlComponent())
                                .build()
                                .toUri();

        ResponseEntity<String> response = restTemplate.postForEntity(sendMessageUri,
                new HttpEntity<>(null), String.class);
    }

    public String getConfirmationCode(String groupId) {

        URI getConfirmationCodeUri = getPreparedBaseUrlComponent().
                queryParam("group_id", groupId)
                .buildAndExpand("groups.getCallbackConfirmationCode")
                .toUri();

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(getConfirmationCodeUri,
                new HttpEntity<>(null), JsonNode.class);

        return Objects.requireNonNull(response.getBody()).get("response").get("code").asText();
    }

    private UriComponentsBuilder getPreparedBaseUrlComponent() {
        int randomId = random.nextInt();

        return UriComponentsBuilder.fromHttpUrl(vkApiUrl + "messages.send")
                .queryParam("v", apiVersion)
                .queryParam("random_id", randomId)
                .queryParam("access_token", token);
    }

}
