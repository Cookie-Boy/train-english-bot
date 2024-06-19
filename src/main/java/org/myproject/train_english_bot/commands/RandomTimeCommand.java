package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.SchedulerService;
import org.myproject.train_english_bot.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

public class RandomTimeCommand extends AdvancedCommand {
    public RandomTimeCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        // doesn't have any realization
    }

    @Override
    public void execute(TelegramBot bot, User user, List<String> args) {
        if (args.size() > 2) {
            bot.sendMessage(user.getChatId(), """
                    A quick question is sent every one or more hours and at the specified minute.
                    Example: /randomtime [hours] [minute].
                    If you don't want to use quick questions, then enter 0 in 'hours'.
                    """);
            return;
        }
        int hour = Integer.parseInt(args.get(0));
        if (hour == 0) {
            bot.sendMessage(user.getChatId(), "Quick questions are turned off for you.");
            return;
        }
        int minute = Integer.parseInt(args.get(1));
//        hour += LocalDateTime.now().getHour();
//        if (hour > 23)
//            hour -= 24;
        LocalDateTime time = SchedulerService.getDateTime(hour, minute);
        userService.setUserQuickNotification(user, time);
    }
}
