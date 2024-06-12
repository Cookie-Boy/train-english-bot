package org.myproject.train_english_bot;

import org.myproject.train_english_bot.commands.Command;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class StopCommand implements Command {
    @Autowired
    private UserService userService;

    @Override
    public void execute(TelegramBot bot, User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.DEFAULT) {
            bot.sendMessage(user.getChatId(), "You are already in default mode.");
            return;
        }
        userService.setUserMode(user, Mode.DEFAULT);
        if (prevMode == Mode.TRAIN) {
            bot.sendMessage(user.getChatId(), "Training has been stopped.");
        } else {
            bot.sendMessage(user.getChatId(), "Adding has been stopped");
        }
    }
}
