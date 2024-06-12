package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.myproject.train_english_bot.service.UserService;

import java.util.List;

public class RemoveCommand extends AdvancedCommand {
    public RemoveCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.REMOVE) {
            bot.sendMessage(user.getChatId(), "You are already in removing mode.");
            return;
        }
        userService.setUserMode(user, Mode.REMOVE);
        bot.sendMessage(user.getChatId(), "OK! Send me a word or translation or its number.");
    }

    @Override
    public void execute(TelegramBot bot, User user, List<String> args) {
        if (args.size() > 1) {
            bot.sendMessage(user.getChatId(), "Use example:\n/remove [number]\n/remove [word]\n/remove [translate]");
            return;
        }
        String arg = args.getFirst();
        bot.handleRemovingWord(user, arg);
    }
}
