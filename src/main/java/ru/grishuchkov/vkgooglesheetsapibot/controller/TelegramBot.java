package ru.grishuchkov.vkgooglesheetsapibot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.api.botName}")
    private String botUsername;

    public TelegramBot(@Value("${telegram.api.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug(update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
