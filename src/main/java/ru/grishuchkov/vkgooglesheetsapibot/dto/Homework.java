package ru.grishuchkov.vkgooglesheetsapibot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Homework {
    private int userId;
    private String screenName;
    private int numberOfHomework;
}
