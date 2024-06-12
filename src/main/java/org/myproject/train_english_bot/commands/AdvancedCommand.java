package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

import java.util.List;

public abstract class AdvancedCommand extends Command {
    public AdvancedCommand(UserService userService) {
        super(userService);
    }

    public abstract void execute(TelegramBot bot, User user, List<String> args);
}
