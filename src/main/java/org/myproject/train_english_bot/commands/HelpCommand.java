package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

public class HelpCommand extends Command {
    public HelpCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        bot.sendMessage(user.getChatId(), "Hello, nice to meet you here.");
    }
}
