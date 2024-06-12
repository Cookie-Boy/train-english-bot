package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.Question;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainingService {
    @Autowired
    private Map<Long, Word> answers;

    public Map<Long, Word> getAnswers() {
        return answers;
    }

    public Word getAnswer(Long chatId) {
        return this.answers.get(chatId);
    }

    public void addAnswer(Long chatId, Word correctAnswer) {
        this.answers.put(chatId, correctAnswer);
    }

    private List<String> getOptions(User user) {
        List<Word> words = user.getWords();
        if (words.size() < 4) {
            return null;
        }

        List<Word> availableWords = words.stream().filter(w -> w.isAvailable()).toList();
        var random = new Random();
        int randomValue = random.nextInt(availableWords.size());
        Word correctAnswer = availableWords.get(randomValue);
        addAnswer(user.getChatId(), correctAnswer);

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

    public Question generateQuestion(User user) {
        Long chatId = user.getChatId();
        List<String> options = getOptions(user);
        if (options == null) {
            return null;
        }
        return new Question(getAnswer(chatId), options);
    }
}
