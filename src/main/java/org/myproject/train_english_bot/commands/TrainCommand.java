package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

public class TrainCommand extends Command {
    public TrainCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.TRAIN) {
            bot.sendMessage(user.getChatId(), "You are already in training mode.");
            return;
        }
        userService.setUserMode(user, Mode.TRAIN);
        userService.enableAllWords(user);
        bot.sendMessage(user.getChatId(), "OK! Let's start training.");
        bot.askQuestion(user);
    }
}
