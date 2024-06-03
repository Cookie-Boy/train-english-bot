package org.myproject.train_english_bot.config;

import lombok.SneakyThrows;
import org.myproject.train_english_bot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SuppressWarnings("unused")
@Configuration
public class TelegramBotConfiguration {
    @Bean
    public TelegramBot telegramBot(
            @Value("${bot.token}") String token,
            @Value("${bot.name}") String name,
            TelegramBotsApi telegramBotsApi
    ) throws TelegramApiException {
        var bot = new TelegramBot(new DefaultBotOptions(), token, name);
        telegramBotsApi.registerBot(bot);
        return bot;
    }

    @Bean
    @SneakyThrows
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}
