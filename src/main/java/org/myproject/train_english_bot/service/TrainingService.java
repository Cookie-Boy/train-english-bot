package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.Question;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainingService {
    @Autowired
    private Map<Long, Question> questions;
    
    public Question getQuestionByChatId(Long chatId) {
        return questions.get(chatId);
    }

    public void putQuestion(Long chatId, Question question) {
        questions.put(chatId, question);
    }

    public Question generateQuestion(User user) {
        List<Word> words = user.getWords();
        if (words.size() < 4) {
            return null;
        }

        List<Word> availableWords = words.stream().filter(Word::isAvailable).toList(); // w -> w.isAvailable()
        var random = new Random();
        int randomValue = random.nextInt(availableWords.size());
        Word answer = availableWords.get(randomValue);

        var options = new ArrayList<String>();
        options.add(answer.getEnVersion());

        while (options.size() < 4) {
            randomValue = random.nextInt(words.size());
            String option = words.get(randomValue).getEnVersion();
            if (options.contains(option)) {
                continue;
            }
            options.add(option);
        }
        Collections.shuffle(options);
        return new Question(options, answer, null);
    }
}
