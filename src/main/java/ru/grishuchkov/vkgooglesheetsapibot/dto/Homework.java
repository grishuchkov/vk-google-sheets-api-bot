package ru.grishuchkov.vkgooglesheetsapibot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Homework {
    private int userId;
    private String screenName;
    private int numberOfHomework;

    public Homework(int userId, int numberOfHomework) {
        this.userId = userId;
        this.numberOfHomework = numberOfHomework;
    }
}
