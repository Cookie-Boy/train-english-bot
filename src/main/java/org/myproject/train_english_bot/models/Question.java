package org.myproject.train_english_bot.models;

import java.util.List;

public class Question {
    private String correctAnswer;
    private List<String> options;

    public Question(String correctAnswer, List<String> options) {
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
