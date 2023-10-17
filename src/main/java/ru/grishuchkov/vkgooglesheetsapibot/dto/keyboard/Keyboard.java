package ru.grishuchkov.vkgooglesheetsapibot.dto.keyboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class Keyboard {

    @JsonProperty("one_time")
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    public Boolean oneTime;

    public List<List<Button>> buttons;
}
