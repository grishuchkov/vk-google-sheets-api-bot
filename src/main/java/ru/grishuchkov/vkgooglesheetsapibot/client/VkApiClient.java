package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public void sendMessage(String userId, String text) {

        URI sendMessageUri = getPreparedBaseUrlComponent()
                .queryParam("message", text)
                .queryParam("user_id", userId)
                .queryParam("keyboard", "")
                .buildAndExpand("messages.send").toUri();

        ResponseEntity<String> response = restTemplate.postForEntity(sendMessageUri,
                new HttpEntity<>(null), String.class);
    }

    public String getConfirmationCode(String groupId){

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

        return UriComponentsBuilder.fromHttpUrl(vkApiUrl)
                .path("{method}")
                .queryParam("v", apiVersion)
                .queryParam("random_id", randomId)
                .queryParam("access_token", token);
    }

}
