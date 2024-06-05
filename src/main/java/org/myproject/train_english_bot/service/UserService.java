package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.UserRepository;
import org.myproject.train_english_bot.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
                new ArrayList<>());
        saveUser(user);
    }

    public void changeWordLevel(User user, Word word, byte value) {
        word.setLevel((byte) (word.getLevel() + value));
        saveUser(user);
    }

    public void toggleWordAvailability(User user, Word word) {
        word.setAvailable(!word.isAvailable());
        saveUser(user);
    }

    public boolean isAllWordsAvailable(User user) {
        return user.getWords().stream().allMatch(w -> w.isAvailable());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}