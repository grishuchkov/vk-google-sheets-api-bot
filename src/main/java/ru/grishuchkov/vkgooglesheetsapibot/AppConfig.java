package ru.grishuchkov.vkgooglesheetsapibot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.grishuchkov.vkgooglesheetsapibot.controller.TelegramBot;

import java.util.ResourceBundle;

@Configuration
@PropertySource(value = "classpath:application.yml")
public class AppConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public VkApiClient vkApiClient(){
        return new VkApiClient(new HttpTransportClient());
    }

    @Bean
    public ResourceBundle messagesBundle(){
        return ResourceBundle.getBundle("messages");
    }

    @Bean
    public BotSession telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
    }
}
