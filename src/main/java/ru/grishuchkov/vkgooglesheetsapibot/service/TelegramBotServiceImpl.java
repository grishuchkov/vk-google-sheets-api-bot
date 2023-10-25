package ru.grishuchkov.vkgooglesheetsapibot.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grishuchkov.vkgooglesheetsapibot.controller.TelegramBot;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.TelegramBotService;

@Service
@Log4j
public class TelegramBotServiceImpl implements TelegramBotService {

    private TelegramBot telegramBot;

    public void registration(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void processMessage(Message message) {
        if (message.getText().equalsIgnoreCase("Дай ID")) {
            String text = String.valueOf(message.getChatId());
            sendMessage(text, text);
        }
    }

    @Override
    public void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.error(ex);
        }
    }
}
