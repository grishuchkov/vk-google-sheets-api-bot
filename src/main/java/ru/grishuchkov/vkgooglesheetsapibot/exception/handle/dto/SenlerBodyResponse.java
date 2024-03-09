package ru.grishuchkov.vkgooglesheetsapibot.exception.handle.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SenlerBodyResponse {
    private List<SenlerVar> vars;
    private List<SenlerVar> globVars;

    public static SenlerBodyResponse construct(String httpCode, String message) {
        return SenlerBodyResponse.builder()
                .vars(List.of(
                        new SenlerVar("http_accept_status_code", httpCode),
                        new SenlerVar("accept_message", message)
                ))
                .build();
    }
}
