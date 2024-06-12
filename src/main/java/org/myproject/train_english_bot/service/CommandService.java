package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.commands.StopCommand;
import org.myproject.train_english_bot.commands.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.*;

@Service
public class CommandService {
    private final Map<String, Command> commandMap;

    public CommandService(UserService userService) {
        this.commandMap = Map.of("/train", new TrainCommand(userService),
                "/add", new AddCommand(userService),
                "/stop", new StopCommand(userService),
                "/words", new WordsCommand(userService),
                "/clear", new ClearCommand(userService),
                "/remove", new RemoveCommand(userService));
    }

    public Command getCommand(String command) {
        return commandMap.get(command);
    }

    public static List<BotCommand> getListOfCommands() {
        return Arrays.asList(
                new BotCommand("/train", "start training mode"),
                new BotCommand("/add", "start adding mode"),
                new BotCommand("/stop", "stops any mode"),
                new BotCommand("/words", "show word list"),
                new BotCommand("/clear", "clear word list"),
                new BotCommand("/remove", "delete one word")
        );
    }
}
