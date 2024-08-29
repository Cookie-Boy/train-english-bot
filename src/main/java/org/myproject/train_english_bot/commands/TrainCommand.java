package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.events.QuestionEvent;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

@Component
public class TrainCommand extends Command {
    @Override
    public void execute(User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.TRAIN) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "You are already in training mode.",
                            null
                    )
            );
            return;
        }
        userService.setUserMode(user, Mode.TRAIN);
        userService.enableAllWords(user);
        eventPublisher.publishEvent(
                new MessageEvent(
                        this,
                        user.getChatId(),
                        "OK! Let's start training.",
                        null
                )
        );
        eventPublisher.publishEvent(
                new QuestionEvent(
                        this,
                        user
                )
        );
    }
}
