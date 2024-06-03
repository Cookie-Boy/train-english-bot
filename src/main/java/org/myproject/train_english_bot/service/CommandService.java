package org.myproject.train_english_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.List;

@Service
public class CommandService {
    public static List<BotCommand> getCommands() {
        return Arrays.asList(
                new BotCommand("/start", "get started"),
                new BotCommand("/words", "show word list"),
                new BotCommand("/stop", "stops any mode"),
                new BotCommand("/deleteall", "clear word list"),
                new BotCommand("/delete", "delete one word"),
                new BotCommand("/train", "start training mode")
        );
    }
}
