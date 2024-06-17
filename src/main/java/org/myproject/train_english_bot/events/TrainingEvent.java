package org.myproject.train_english_bot.events;

import org.myproject.train_english_bot.models.User;
import org.springframework.context.ApplicationEvent;

public class TrainingEvent extends ApplicationEvent {
    private final User user;

    public TrainingEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
