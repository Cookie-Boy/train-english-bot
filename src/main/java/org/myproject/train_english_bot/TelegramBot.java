package org.myproject.train_english_bot;

import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.myproject.train_english_bot.service.CommandService;
import org.myproject.train_english_bot.service.KeyboardService;
import org.myproject.train_english_bot.service.TrainingService;
import org.myproject.train_english_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
            User user = optionalUser.get();

            if (user.getMode() == Mode.TRAIN) {
                Word correctAnswer = trainingService.getCorrectAnswer(chatId);
                String resultPart = String.format("Translation for %s is %s",
                        correctAnswer.getRuVersion(),
                        correctAnswer.getEnVersion());

                if (text.equals(correctAnswer.getEnVersion())) {
                    sendMessage(chatId, "✅ Correct! " + resultPart);
                    userService.changeWordLevel(user, correctAnswer, (byte) 1);
                    userService.toggleWordAvailability(user, correctAnswer);
                } else {
                    sendMessage(chatId, "❌ You are wrong! " + resultPart);
                    userService.changeWordLevel(user, correctAnswer, (byte) -1);
                }

                if (!userService.isAllWordsAvailable(user)) {
                    sendMessage(chatId, "All words are repeated. The training is over.");
                    user.setMode(Mode.DEFAULT);
                    userService.saveUser(user);
                    return;
                }
                List<String> options = trainingService.generateQuestion(user);
                if (options == null) {
                    sendMessage(chatId, "You don't have enough words to learn! Need at least 4 words.");
                    user.setMode(Mode.DEFAULT);
                    userService.saveUser(user);
                    return;
                }

                correctAnswer = trainingService.getCorrectAnswer(chatId);
                var keyboard = KeyboardService.getTrainingKeyboard(options);
                sendMessage(chatId,
                        "Translation for " + correctAnswer.getRuVersion() + ": ",
                        keyboard);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void sendMessage(Long chatId, String text) {
        sendMessage(new SendMessage(chatId.toString(), text));
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        var message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboard);
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