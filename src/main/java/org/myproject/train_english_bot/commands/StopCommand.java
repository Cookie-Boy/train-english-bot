package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

@Component
public class StopCommand extends Command {
    @Override
    public void execute(User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.DEFAULT) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "You are already in default mode.",
                            null
                    )
            );
            return;
        }
        userService.setUserMode(user, Mode.DEFAULT);
        String text;
        switch (prevMode) {
            case TRAIN -> text = "Training has been stopped.";
            case TRAIN_ONCE -> text = "Mini-training has been stopped.";
            case ADD -> text = "Adding has been stopped.";
            case REMOVE -> text = "Removing has been stopped.";
            default -> text = "You are not in any mode.";
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
