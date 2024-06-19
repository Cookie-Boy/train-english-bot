package org.myproject.train_english_bot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Entity(name = "user-table")
public class User {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private LocalDateTime trainingNotification;

    private LocalDateTime quickNotification;

    private int quickInterval;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Word> words;
}
