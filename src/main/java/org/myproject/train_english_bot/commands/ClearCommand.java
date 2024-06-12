package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

public class ClearCommand extends Command {
    public ClearCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        user.getWords().clear();
        bot.sendMessage(user.getChatId(), "All your words have been successfully deleted.");
    }
}
