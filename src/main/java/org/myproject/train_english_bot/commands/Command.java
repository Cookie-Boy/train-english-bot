package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class Command {
    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    protected UserService userService;

    public abstract void execute(User user);
}
