package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.myproject.train_english_bot.service.UserService;

import java.util.List;

public class WordsCommand extends Command {
    public WordsCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        Long chatId = user.getChatId();
        List<Word> words = user.getWords();
        if (words.isEmpty()) {
            bot.sendMessage(chatId, "You don't have any words to learn yet.");
        } else {
            String text = "OK! Here is the list of your words: \n\n";
            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);
                text = text.concat(String.format("%d. %s - %s\n",
                        i + 1,
                        word.getEnVersion(),
                        word.getRuVersion()));
            }
            text = text.substring(0, text.length() - 1);
            bot.sendMessage(chatId, text);
        }
    }
}
