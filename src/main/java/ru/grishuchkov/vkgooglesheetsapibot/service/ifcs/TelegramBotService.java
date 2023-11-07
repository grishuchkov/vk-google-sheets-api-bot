package ru.grishuchkov.vkgooglesheetsapibot.service.ifcs;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.grishuchkov.vkgooglesheetsapibot.controller.TelegramBot;

public interface TelegramBotService {
    void registration(TelegramBot telegramBot);

    void processMessage(Message message);

    void sendMessage(String chatId, String text);
}
