package ru.grishuchkov.vkgooglesheetsapibot.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.grishuchkov.vkgooglesheetsapibot.controller.TelegramBot;
import ru.grishuchkov.vkgooglesheetsapibot.service.ifcs.TelegramBotService;
import ru.grishuchkov.vkgooglesheetsapibot.utils.MessageUtils;

@Service
@Log4j
public class TelegramBotServiceImpl implements TelegramBotService {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;

    public TelegramBotServiceImpl(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }
    public void registration(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void processMessage(Message message) {
        if (message.getText() == null) {
            log.error("Message is null");
        }

        if (message.getText().equalsIgnoreCase("Дай ID")) {
            String id = String.valueOf(message.getChatId());
            String text = id;

            sendMessage(id, text);
        }
    }

    @Override
    public void sendMessage(String chatId, String text) {
        executeMessage(new SendMessage(chatId, text));
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.error(ex);
        }
    }
}
