package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.events.MessageEvent;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.SchedulerService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RandomTimeCommand extends AdvancedCommand {
    @Override
    public void execute(User user, List<String> args) {
        if (args.size() > 2) {
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            """
                            A quick question is sent every one or more hours and at the specified minute.
                            Example: /randomtime [hours] [minute].
                            If you don't want to use quick questions, then enter 0 in 'hours'.
                            """,
                            null
                    )
            );
            return;
        }
        int hour = Integer.parseInt(args.get(0));
        if (hour == 0) {
            LocalDateTime time = SchedulerService.getDateTime(0, 0);
            userService.setUserQuickNotification(user, time);
            eventPublisher.publishEvent(
                    new MessageEvent(
                            this,
                            user.getChatId(),
                            "Quick questions are turned off for you.",
                            null
                    )
            );
            return;
        }
        int minute = Integer.parseInt(args.get(1));
//        hour += LocalDateTime.now().getHour();
//        if (hour > 23)
//            hour -= 24;
        LocalDateTime time = SchedulerService.getDateTime(hour, minute);
        userService.setUserQuickNotification(user, time);
    }

    @Override
    public void execute(User user) {
        // doesn't have any realization
    }
}
