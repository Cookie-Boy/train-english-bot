package org.myproject.train_english_bot.service;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.UserRepository;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Service позволяет НЕ СОЗДАВАТЬ экземпляры классов (new User()), где используется сервис
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(Long chatId) {
        return userRepository.findById(chatId);
    }

    public void addUser(Long chatId) {
        User user = new User(chatId,
                Mode.DEFAULT,
                new Timestamp(System.currentTimeMillis()),
                new ArrayList<Word>());
        saveUser(user);
    }

    @Transactional
    public void addUserWord(User user, Word word) {
        user.getWords().add(word);
        saveUser(user);
    }

    @Transactional
    public Word adjustWordLevel(User user, Word word, byte value) {
        Optional<Word> optionalWord = findWordInList(user.getWords(), word);
        if (optionalWord.isEmpty())
            return null;
        Word foundWord = optionalWord.get();
        byte currentLevel = foundWord.getLevel();
        if (currentLevel >= 0) {
            foundWord.setLevel((byte) (currentLevel + value));
            saveUser(user);
        }
        return foundWord;
    }

    @Transactional
    public Word toggleWordAvailability(User user, Word word) {
        Optional<Word> optionalWord = findWordInList(user.getWords(), word);
        if (optionalWord.isEmpty()) {
            System.out.println("here");
            return null;
        }
        Word foundWord = optionalWord.get();
        foundWord.setAvailable(!foundWord.isAvailable());
        saveUser(user);
        return foundWord;
    }

    @Transactional
    public void enableAllWords(User user) {
        user.getWords().forEach(w -> w.setAvailable(true));
        saveUser(user);
    }

    @Transactional
    public boolean areAllWordsNotAvailable(User user) {
        return user.getWords().stream().noneMatch(w -> w.isAvailable());
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    private Optional<Word> findWordInList(List<Word> words, Word word) {
        return words.stream().filter(w -> w.equals(word)).findFirst();
    }
}