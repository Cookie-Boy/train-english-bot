package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WordsCommand extends Command {
    @Override
    public void execute(User user) {
        List<Word> words = user.getWords();
        String text;
        if (words.isEmpty()) {
            text = "You don't have any words to learn yet.";
        } else {
            text = "OK! Here is the list of your words: \n\n";
            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);
                text = text.concat(String.format("%d. %s - %s\n",
                        i + 1,
                        word.getEnVersion(),
                        word.getRuVersion()));
            }
            text = text.substring(0, text.length() - 1);
        }
        eventPublisher.publishEvent(
                new MessageEvent(
                        this,
                        user.getChatId(),
                        text,
                        null
                )
        );
    }
}
