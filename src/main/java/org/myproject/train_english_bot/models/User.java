package org.myproject.train_english_bot.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity(name = "user-table")
public class User {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private Timestamp nextNotice;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Word> words;

    public User(Long chatId, Mode mode, Timestamp nextNotice, List<Word> words) {
        this.chatId = chatId;
        this.mode = mode;
        this.nextNotice = nextNotice;
        this.words = words;
    }

    public User() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Timestamp getNextNotice() {
        return nextNotice;
    }

    public void setNextNotice(Timestamp nextNotice) {
        this.nextNotice = nextNotice;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
