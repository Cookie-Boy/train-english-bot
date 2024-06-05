package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TrainingService {
    @Autowired
    private Map<Long, Word> correctAnswers;

    public Map<Long, Word> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Map<Long, Word> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Word getCorrectAnswer(Long chatId) {
        return this.correctAnswers.get(chatId);
    }

    public void addCorrectAnswer(Long chatId, Word correctAnswer) {
        this.correctAnswers.put(chatId, correctAnswer);
    }

    public void removeCorrectAnswer(Long chatId) {
        this.correctAnswers.remove(chatId);
    }

    public List<String> generateQuestion(User user) {
        var words = user.getWords();
        if (words.size() < 4) {
            return null;
        }



    }
}
