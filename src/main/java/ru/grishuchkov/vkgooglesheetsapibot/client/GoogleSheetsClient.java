package ru.grishuchkov.vkgooglesheetsapibot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
@Log4j
public class GoogleSheetsClient implements GoogleSheetsApiClient {

    private final RestTemplate restTemplate;

    @Value("${google.sheet.api.url}")
    private String urlGoogleSheetsApi;

    @Override
    public String sendHomework(Homework homework) throws JsonProcessingException {
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

            JsonNode body = new ObjectMapper().readTree(result);

            if (body.get("statusCode").asInt() == HttpStatus.OK.value()){
                log.debug("sendHomework method was executed successfully");
                return "ok";
            }
        }

        log.error("Google returned bad status code at sendHomework()");
        throw new BadStatusCodeException("Google returned bad status code at sendHomework()");
    }
}
