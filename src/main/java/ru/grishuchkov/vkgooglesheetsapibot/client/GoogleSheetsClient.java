package ru.grishuchkov.vkgooglesheetsapibot.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.grishuchkov.vkgooglesheetsapibot.client.ifcs.GoogleSheetsApiClient;
import ru.grishuchkov.vkgooglesheetsapibot.dto.Homework;
import ru.grishuchkov.vkgooglesheetsapibot.dto.SendHomeworkResponse;
import ru.grishuchkov.vkgooglesheetsapibot.exception.BadStatusCodeException;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Log4j
public class GoogleSheetsClient implements GoogleSheetsApiClient {

    private final RestTemplate restTemplate;

    @Value("${google.sheet.api.url}")
    private String urlGoogleSheetsApi;

    @Override
    public SendHomeworkResponse sendHomework(Homework homework){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(urlGoogleSheetsApi)
                .queryParam("action", "addHomework")
                .queryParam("userScreenName", homework.getScreenName())
                .queryParam("numberOfHomework", homework.getNumberOfHomework())
                .build()
                .toUri();

        ResponseEntity<String> sendResponse = restTemplate.postForEntity(uri, new HttpEntity<>(null), String.class);

        if(sendResponse.getStatusCode() != HttpStatus.FOUND){
           throw new RuntimeException("Internal exception");
        }

        String redirectUrl = sendResponse.getHeaders().getLocation().toString();
        ResponseEntity<SendHomeworkResponse> redirectedResponse = restTemplate.getForEntity(redirectUrl, SendHomeworkResponse.class);

        SendHomeworkResponse body = redirectedResponse.getBody();

        if (body.getStatusCode() != HttpStatus.OK.value()){
            log.error("Google returned bad status code at sendHomework()");
            throw new BadStatusCodeException("Google returned bad status code at sendHomework()");
        }

        log.info("sendHomework method was executed successfully");
        return body;
    }
}
