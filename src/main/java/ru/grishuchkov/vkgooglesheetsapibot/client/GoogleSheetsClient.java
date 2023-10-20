package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.GoogleSheetsApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.exception.BadStatusCodeException;
import ru.grishuchkov.vkgooglesheetsapibot.exception.JsonParseFiledException;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoogleSheetsClient implements GoogleSheetsApiClient {

    private final RestTemplate restTemplate;

    @Value("${googleSheetApiUrl}")
    private String urlGoogleSheetsApi;

    @Override
    public String sendHomework(Homework homework) {

        URI uri = UriComponentsBuilder
                .fromHttpUrl(urlGoogleSheetsApi)
                .queryParam("action", "addHomework")
                .queryParam("userScreenName", homework.getScreenName())
                .queryParam("numberOfHomework", homework.getNumberOfHomework())
                .build()
                .toUri();

        ResponseEntity<String> response = restTemplate.postForEntity(uri, new HttpEntity<>(null), String.class);

        String redirectUrl = Objects.requireNonNull(response.getHeaders().getLocation()).toString();
        ResponseEntity<String> redirectedResponse = restTemplate.getForEntity(redirectUrl, String.class);

        JsonNode body;
        try {
            body = new ObjectMapper().readTree(redirectedResponse.getBody());
        } catch (JsonProcessingException e) {
            throw new JsonParseFiledException();
        }

        if (body.get("statusCode").asInt() != HttpStatus.OK.value()){
            throw new BadStatusCodeException("Google returned bad status code at sendHomework()");
        }

        return "ok";
    }
}
