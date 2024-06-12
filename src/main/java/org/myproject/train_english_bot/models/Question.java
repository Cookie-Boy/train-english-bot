package org.myproject.train_english_bot.models;

import java.util.List;

public class Question {
    private Word answer;
    private List<String> options;

    public Question(Word answer, List<String> options) {
        this.answer = answer;
        this.options = options;
    }

    public Word getAnswer() {
        return answer;
    }

    public void setAnswer(Word answer) {
        this.answer = answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
