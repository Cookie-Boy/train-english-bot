package org.myproject.train_english_bot.events;

import lombok.Getter;
import org.myproject.train_english_bot.models.User;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddEvent extends ApplicationEvent {
    private final User user;
    private final String text;

    public AddEvent(Object source, User user, String text) {
        super(source);
        this.user = user;
        this.text = text;
    }
}
