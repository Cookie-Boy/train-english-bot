package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void removeCorrectAnswer(User user) {
        this.correctAnswers.remove(user);
    }

    public List<String> generateQuestion(User user) {
        List<Word> words = user.getWords();
        if (words.size() < 4) {
            return null;
        }

        List<Word> availableWords = words.stream().filter(w -> w.isAvailable()).toList();
        var random = new Random();
        int randomValue = random.nextInt(availableWords.size());
        Word correctAnswer = availableWords.get(randomValue);
        addCorrectAnswer(user.getChatId(), correctAnswer);

        var options = new ArrayList<String>();
        options.add(correctAnswer.getEnVersion());

        while (options.size() < 4) {
            randomValue = random.nextInt(words.size());
            String option = words.get(randomValue).getEnVersion();
            if (options.contains(option)) {
                continue;
            }
            options.add(option);
        }
        Collections.shuffle(options);
        return options;
    }
}
