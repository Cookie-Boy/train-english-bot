package org.myproject.train_english_bot.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Getter
public class MessageEvent extends ApplicationEvent {
    private final Long chatId;
    private final String text;
    private final ReplyKeyboardMarkup keyboard;

    public MessageEvent(Object source, Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        super(source);
        this.chatId = chatId;
        this.text = text;
        this.keyboard = keyboard;
    }
}
