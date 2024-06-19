package org.myproject.train_english_bot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Question {
    private List<String> options;
    private Word answer;
    private Integer messageId;
}
