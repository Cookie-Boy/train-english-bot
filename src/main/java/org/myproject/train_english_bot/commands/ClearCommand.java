package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

@Component
public class ClearCommand extends Command {
    @Override
    public void execute(User user) {
        user.getWords().clear();
        eventPublisher.publishEvent(
                new MessageEvent(
                        this,
                        user.getChatId(),
                        "All your words have been successfully deleted.",
                        null
                )
        );
    }
}
