package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.GoogleSheetsApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class GoogleSheetsClient implements GoogleSheetsApiClient {

    private final RestTemplate restTemplate;

    @Value("${googleSheetApiUrl}")
    private String urlGoogleSheetsApi;

    @Override
    @SneakyThrows
    public String sendHomework(Homework homework) {

        URI uri = UriComponentsBuilder
                .fromHttpUrl(urlGoogleSheetsApi)
                .queryParam("action", "addHomework")
                .queryParam("userScreenName", homework.getScreenName())
                .queryParam("numberOfHomework", homework.getNumberOfHomework())
                .build()
                .toUri();

        ResponseEntity<String> response = restTemplate.postForEntity(uri, new HttpEntity<>(null), String.class);

        if(response.getStatusCode() == HttpStatus.FOUND){
            String redirectUrl = response.getHeaders().getLocation().toString();
            ResponseEntity<String> redirectedResponse = restTemplate.getForEntity(redirectUrl, String.class);
            String result = redirectedResponse.getBody();

            JsonNode json = new ObjectMapper().readTree(result);

            int statusCode = json.get("statusCode").asInt();
        }

        return "ok";
    }
}
