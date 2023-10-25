package ru.grishuchkov.vkgooglesheetsapibot.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.TelegramBotService;


@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.api.botName}")
    private String botUsername;
    private final TelegramBotService telegramService;

    public TelegramBot(@Value("${telegram.api.token}") String token, TelegramBotService telegramService) {
        super(token);
        this.telegramService = telegramService;
    }

    @PostConstruct
    public void init(){
        telegramService.registration(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received message from user");

        if (update.hasMessage()) {
            telegramService.processMessage(update.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
