package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;

import java.util.List;

public class AddCommand extends AdvancedCommand {
    public AddCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.ADD) {
            bot.sendMessage(user.getChatId(), "You are already in adding mode.");
            return;
        }
        userService.setUserMode(user, Mode.ADD);
        bot.sendMessage(user.getChatId(), "OK! Just send me a word and translation for it.");
    }

    @Override
    public void execute(TelegramBot bot, User user, List<String> args) {
        if (args.size() > 2) {
            bot.sendMessage(user.getChatId(), "Use example:\n/add [word] [translate]\n/add [translate] [word]");
            return;
        }
        bot.handleAddingWord(user, String.join(" ", args));
    }
}
