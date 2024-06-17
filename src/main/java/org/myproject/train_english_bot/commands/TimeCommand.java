package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.TelegramBot;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.SchedulerService;
import org.myproject.train_english_bot.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

public class TimeCommand extends AdvancedCommand {
    public TimeCommand(UserService userService) {
        super(userService);
    }

    @Override
    public void execute(TelegramBot bot, User user) {
        // doesn't have realization
    }

    @Override
    public void execute(TelegramBot bot, User user, List<String> args) {
        if (args.size() > 2) {
            bot.sendMessage(user.getChatId(), "Use example:\n/time [hour] [minute]");
            return;
        }
        var time = SchedulerService.getDateTime(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
        userService.setUserTrainingNotice(user, time);
    }
}
