package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends Command {
    @Override
    public void execute(User user) {
        eventPublisher.publishEvent(
                new MessageEvent(
                        this,
                        user.getChatId(),
                        "Hello, nice to meet you here.",
                        null
                )
        );
    }
}
