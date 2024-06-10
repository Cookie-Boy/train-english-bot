package org.myproject.train_english_bot;

import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.myproject.train_english_bot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingService trainingService;

    public TelegramBot(DefaultBotOptions options, String botToken, String botName) {
        super(options, botToken);
        this.botName = botName;

        var listOfCommands = CommandService.getCommands();
        setCommands(listOfCommands);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().toLowerCase();
            var chatId = update.getMessage().getChatId();

            var optionalUser = userService.getUser(chatId);
            if (optionalUser.isEmpty()) {
                sendMessage(chatId, "Hello, nice to meet you here!");
                userService.addUser(chatId);
                return;
            }
            User user = optionalUser.get(); // ВСЕ ВРЕМЯ СОЗДАЕТСЯ ЛОКАЛЬНАЯ ПЕРЕМЕННАЯ
            Mode mode = user.getMode();

            if (mode == Mode.TRAIN) {
                if (text.equals("/stop")) {
                    sendMessage(chatId, "Training has been stopped.");
                    user.setMode(Mode.DEFAULT);
                    userService.saveUser(user);
                    return;
                }

                Word correctAnswer = trainingService.getCorrectAnswer(chatId);
                System.out.println("HERE: " + correctAnswer.isAvailable());
                String resultPart = String.format("Translation for %s is %s",
                        correctAnswer.getRuVersion(),
                        correctAnswer.getEnVersion());

                if (text.equals(correctAnswer.getEnVersion())) {
                    sendMessage(chatId, "✅ Correct! " + resultPart);
                    correctAnswer = userService.adjustWordLevel(user, correctAnswer, (byte) 1);
                    correctAnswer = userService.toggleWordAvailability(user, correctAnswer);
                } else {
                    sendMessage(chatId, "❌ You are wrong! " + resultPart);
                    correctAnswer = userService.adjustWordLevel(user, correctAnswer, (byte) -1);
                }

                if (userService.areAllWordsNotAvailable(user)) {
                    sendMessage(chatId, "All words are repeated. The training is over.", null);
                    user.setMode(Mode.DEFAULT);
                    for (Word word : user.getWords()) {
                        userService.toggleWordAvailability(user, word);
                    }
                    userService.saveUser(user);
                    return;
                }
                askQuestion(user);
            } else if (mode == Mode.ADD) {
                if (text.equals("/stop")) {
                    sendMessage(chatId, "Adding has been stopped.");
                    user.setMode(Mode.DEFAULT);
                    userService.saveUser(user);
                    return;
                }

                Word word = AddingService.getLearningWord(text);
                if (word == null) {
                    sendMessage(chatId, "Use example:\n'word' 'translate' " +
                            "(space between two words, without numbers)");
                    return;
                }
                userService.addUserWord(user, word);
            } else if (mode == Mode.DEFAULT) {
                switch (text) {
                        case "practice" -> {
                        user.setMode(Mode.TRAIN);
                        userService.enableAllWords(user);
                        userService.saveUser(user);
                        sendMessage(chatId, "OK! Let's start training.");
                        askQuestion(user);
                    }
                    case "some new words" -> {
                        user.setMode(Mode.ADD);
                        userService.saveUser(user);
                        sendMessage(chatId, "OK! Just send me a word and translation for it.");
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void askQuestion(User user) {
        Long chatId = user.getChatId();
        List<String> options = trainingService.generateQuestion(user);
        if (options == null) {
            sendMessage(chatId, "You don't have enough words to learn! Need at least 4 words.");
            user.setMode(Mode.DEFAULT);
            userService.saveUser(user);
            return;
        }
        Word correctAnswer = trainingService.getCorrectAnswer(chatId);
        var keyboard = KeyboardService.getTrainingKeyboard(options);
        sendMessage(chatId,
                "Translation for " + correctAnswer.getRuVersion() + ": ",
                keyboard);
    }

    private void sendMessage(Long chatId, String text) {
        sendMessage(new SendMessage(chatId.toString(), text));
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        var message = new SendMessage(chatId.toString(), text);
        if (keyboard == null) {
            var removeKeyboard = new ReplyKeyboardRemove();
            removeKeyboard.setRemoveKeyboard(true);
            message.setReplyMarkup(removeKeyboard);
        } else {
            message.setReplyMarkup(keyboard);
        }
        sendMessage(message);
    }

    private void sendMessage(SendMessage message) {
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            logger.severe("An error occurred while sending a message: " + e.getMessage());
        }
    }

    private void setCommands(List<BotCommand> listOfCommands) {
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logger.severe("An error occurred while setting the commands: " + e.getMessage());
        }
    }
}