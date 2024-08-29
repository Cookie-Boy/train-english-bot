package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.SchedulerService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainTimeCommand extends AdvancedCommand {
    @Override
    public void execute(User user, List<String> args) {
        if (args.size() > 2) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "Use example:\n/time [hour] [minute]",
                            null
                    )
            );
            return;
        }
        var time = SchedulerService.getDateTime(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
        userService.setUserTrainingNotification(user, time);
    }

    @Override
    public void execute(User user) {
        // doesn't have realization
    }
}
