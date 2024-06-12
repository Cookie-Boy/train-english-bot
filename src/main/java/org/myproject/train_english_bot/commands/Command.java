package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

public abstract class Command {
    UserService userService;

    public Command(UserService userService) {
        this.userService = userService;
    }

    public abstract void execute(TelegramBot bot, User user);
}
