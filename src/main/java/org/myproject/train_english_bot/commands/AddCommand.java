package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.AddEvent;
import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddCommand extends AdvancedCommand {
    @Override
    public void execute(User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.ADD) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "You are already in adding mode.",
                            null
                    )
            );
            return;
        }
        userService.setUserMode(user, Mode.ADD);
        eventPublisher.publishEvent(
                new MessageEvent(
                    this,
                    user.getChatId(),
                    "OK! Just send me a word and translation for it.",
                    null
                )
        );
    }

    @Override
    public void execute(User user, List<String> args) {
        if (args.size() > 2) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "Use example:\n/add [word] [translate]\n/add [translate] [word]",
                            null
                    )
            );
            return;
        }
        eventPublisher.publishEvent(
                new AddEvent(
                        this,
                        user,
                        String.join(" ", args)
                )
        );
    }
}
