package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.commands.StopCommand;
import org.myproject.train_english_bot.commands.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.*;

@Service
public class CommandService {
    private final Map<String, Command> commandMap;

    public CommandService(Map<String, Command> commands) {
        this.commandMap = Map.of(
                "/train", commands.get("trainCommand"),
                "/add", commands.get("addCommand"),
                "/stop", commands.get("stopCommand"),
                "/words", commands.get("wordsCommand"),
                "/clear", commands.get("clearCommand"),
                "/help", commands.get("helpCommand"),
                "/remove", commands.get("removeCommand"),
                "/traintime", commands.get("trainTimeCommand"),
                "/randomtime", commands.get("randomTimeCommand"));
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
                new BotCommand("/help", "show help message"),
                new BotCommand("/remove", "delete one word"),
                new BotCommand("/traintime", "set time for daily training notifications"),
                new BotCommand("/randomtime", "set time for one-time training")
        );
    }
}
