package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.events.RemoveEvent;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoveCommand extends AdvancedCommand {
    @Override
    public void execute(User user, List<String> args) {
        if (args.size() > 1) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "Use example:\n/remove [number]\n/remove [word]\n/remove [translate]",
                            null
                    )
            );
            return;
        }
        String arg = args.getFirst();
        eventPublisher.publishEvent(
                new RemoveEvent(
                        this,
                        user,
                        arg
                )
        );
    }

    @Override
    public void execute(User user) {
        Mode prevMode = user.getMode();
        if (prevMode == Mode.REMOVE) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "You are already in removing mode.",
                            null
                    )
            );
            return;
        }
        userService.setUserMode(user, Mode.REMOVE);
        eventPublisher.publishEvent(
                new MessageEvent(
                        this,
                        user.getChatId(),
                        "OK! Send me a word or translation or its number.",
                        null
                )
        );
    }
}
