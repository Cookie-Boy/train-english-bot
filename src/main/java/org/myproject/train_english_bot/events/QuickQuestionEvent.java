package org.myproject.train_english_bot.events;

import org.myproject.train_english_bot.models.User;

public class QuickQuestionEvent extends QuestionEvent {
    public QuickQuestionEvent(Object source, User user) {
        super(source, user);
    }
}
