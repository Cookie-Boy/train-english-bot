package org.myproject.train_english_bot.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "user-table")
public class User {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private LocalDateTime trainingNotice;

    private LocalDateTime randomNotice;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Word> words;

    public User(Long chatId, Mode mode, LocalDateTime trainingNotice, LocalDateTime randomNotice, List<Word> words) {
        this.chatId = chatId;
        this.mode = mode;
        this.trainingNotice = trainingNotice;
        this.randomNotice = randomNotice;
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

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public LocalDateTime getTrainingNotice() {
        return trainingNotice;
    }

    public void setTrainingNotice(LocalDateTime trainingNotice) {
        this.trainingNotice = trainingNotice;
    }

    public LocalDateTime getRandomNotice() {
        return randomNotice;
    }

    public void setRandomNotice(LocalDateTime randomNotice) {
        this.randomNotice = randomNotice;
    }
}
