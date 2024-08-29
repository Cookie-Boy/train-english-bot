package org.myproject.train_english_bot;

import org.myproject.train_english_bot.commands.AdvancedCommand;
import org.myproject.train_english_bot.commands.Command;
import org.myproject.train_english_bot.events.*;
import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.models.Question;
import org.myproject.train_english_bot.models.User;
import org.myproject.train_english_bot.models.Word;
import org.myproject.train_english_bot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private CommandService commandService;

    @Autowired
    private SchedulerService schedulerService;

    public TelegramBot(DefaultBotOptions options, String botToken, String botName) {
        super(options, botToken);
        this.botName = botName;

        var listOfCommands = CommandService.getListOfCommands();
        setCommands(listOfCommands);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        String text = update.getMessage().getText().toLowerCase();
        Long chatId = update.getMessage().getChatId();

        var optionalUser = userService.getUser(chatId);
        if (optionalUser.isEmpty()) {
            sendMessage(chatId, "Hello, nice to meet you here!");
            User user = userService.addUser(chatId);
            var time = SchedulerService.getDefaultTrainingTime();
            userService.setUserTrainingNotification(user, time);
            time = SchedulerService.getDateTime(18, 10);
            userService.setUserQuickNotification(user, time);
            return;
        }
        User user = optionalUser.get(); // ВСЕ ВРЕМЯ СОЗДАЕТСЯ ЛОКАЛЬНАЯ ПЕРЕМЕННАЯ
        Mode mode = user.getMode();

        List<String> args = Arrays.stream(text.split(" ")).toList();
        if (args.getFirst().charAt(0) == '/') {
            Command command = commandService.getCommand(args.getFirst());
            if (command != null) {
                if (args.size() == 1) {
                    command.execute(user);
                } else {
                    AdvancedCommand advancedCommand = (AdvancedCommand)command;
                    advancedCommand.execute(user, args.subList(1, args.size()));
                }
                return;
            }
        }

        switch (mode) {
            case TRAIN -> {
                Word answer = trainingService.getQuestionByChatId(chatId).getAnswer();
                String resultPart = String.format("Translation for '%s' is '%s'",
                        answer.getRuVersion(),
                        answer.getEnVersion());

                if (text.equals(answer.getEnVersion())) {
                    sendMessage(chatId, "✅ Correct! " + resultPart);
                    answer = userService.adjustWordLevel(user, answer, (byte) 1);
                    answer = userService.toggleWordAvailability(user, answer);
                } else {
                    sendMessage(chatId, "❌ You are wrong! " + resultPart);
                    answer = userService.adjustWordLevel(user, answer, (byte) -1);
                }

                if (userService.areAllWordsNotAvailable(user)) {
                    sendMessage(chatId, "All words are repeated. The training is over.", null);
                    userService.setUserMode(user, Mode.DEFAULT);
                    for (Word word : user.getWords()) {
                        userService.toggleWordAvailability(user, word);
                    }
                    return;
                }
                askQuestion(user);
            }
            case TRAIN_ONCE -> {
                Question question = trainingService.getQuestionByChatId(chatId);
                Word answer = question.getAnswer();
                String resultPart = String.format("Translation for '%s' is '%s'",
                        answer.getRuVersion(),
                        answer.getEnVersion());

                if (text.equals(answer.getEnVersion())) {
                    sendMessage(chatId, "✅ Correct! " + resultPart);
                } else {
                    sendMessage(chatId, "❌ You are wrong! " + resultPart);
                }
                userService.increaseUserQuickNotification(user);
                schedulerService.removeMessage(question.getMessageId());
                userService.setUserMode(user, Mode.DEFAULT);
            }
            case ADD -> handleAddingWord(user, text);
            case REMOVE -> {
                if (args.size() != 1) {
                    sendMessage(chatId, "You have to send me only one word or index.");
                    return;
                }
                handleRemovingWord(user, args.getFirst());
            }
        }
    }

    @EventListener
    private void handleAddingWord(AddEvent event) {
        handleAddingWord(event.getUser(), event.getText());
    }

    public void handleAddingWord(User user, String text) {
        Long chatId = user.getChatId();
        Word word = WordService.getLearningWord(text);
        if (word == null) {
            sendMessage(chatId, "Use example:\n'word' 'translate' " +
                    "(space between two words, without numbers)");
            return;
        }
        userService.addUserWord(user, word);
        sendMessage(chatId, "Word has been added.");
    }

    @EventListener
    private void handleRemovingWord(RemoveEvent event) {
        handleRemovingWord(event.getUser(), event.getText());
    }

    public void handleRemovingWord(User user, String word) {
        Long chatId = user.getChatId();
        if (word.chars().allMatch(Character::isDigit)) {
            try {
                Word delWord = userService.removeUserWord(user, Integer.parseInt(word) - 1);
                if (delWord == null) {
                    sendMessage(chatId, "The number is out of range.");
                    return;
                }
                sendMessage(chatId, "Word '" + delWord.getEnVersion() + "' ('"
                        + delWord.getRuVersion() + "') removed.");
            } catch (NumberFormatException e) {
                sendMessage(chatId, "This value can't be processed (it may be too large).");
            }
        } else {
            Word delWord = userService.removeUserWord(user, word);
            if (delWord == null) {
                sendMessage(chatId, "Word not found.");
                return;
            }
            sendMessage(chatId, "Word '" + delWord.getEnVersion() + "' ('"
                    + delWord.getRuVersion() + "') removed.");
        }
    }

    public void askQuestion(User user) {
        Long chatId = user.getChatId();
        Question question = trainingService.generateQuestion(user);
        if (question == null) {
            sendMessage(chatId, "You don't have enough words to learn! Need at least 4 words.");
            userService.setUserMode(user, Mode.DEFAULT);
            return;
        }
        var keyboard = KeyboardService.getTrainingKeyboard(question.getOptions());
        sendMessage(chatId,
                "Translation for '" + question.getAnswer().getRuVersion() + "': ",
                keyboard);
        trainingService.putQuestion(chatId, question);
    }

    @EventListener
    private void handleMessageEvent(MessageEvent event) {
        sendMessage(event.getChatId(), event.getText(), event.getKeyboard());
    }

    @EventListener
    private void handleQuestionEvent(QuestionEvent event) {
        askQuestion(event.getUser());
    }

    @EventListener
    private void handleQuickQuestionEvent(QuickQuestionEvent event) {
        User user = event.getUser();
        Long chatId = user.getChatId();
        userService.setUserMode(user, Mode.TRAIN_ONCE);
        Question question = trainingService.generateQuestion(user);
        if (question == null)
            return;

        var keyboard = KeyboardService.getTrainingKeyboard(question.getOptions());
        Message message = sendDisappearingMessage(
                chatId,
                "Translation for '" + question.getAnswer().getRuVersion() + "': ",
                keyboard,
                60);
        Integer messageId = message.getMessageId();
        question.setMessageId(messageId);
        schedulerService.putMessage(messageId, chatId);
        trainingService.putQuestion(chatId, question);
    }

    public Message sendDisappearingMessage(Long chatId, String text, int delay) {
        return sendDisappearingMessage(chatId, text, null, delay);
    }

    public Message sendDisappearingMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard, int delay) {
        Message message = sendMessage(chatId, text, keyboard);
        Integer messageId = message.getMessageId();
        var scheduler = schedulerService.getScheduler();

        scheduler.schedule(() -> {
            if (!schedulerService.getSentMessages().containsKey(messageId))
                return;

            Optional<User> optionalUser = userService.getUser(chatId);
            if (optionalUser.isEmpty())
                return;

            User user = optionalUser.get();
            userService.setUserMode(user, Mode.DEFAULT);
            var deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);

            try {
                this.execute(deleteMessage);
            } catch (TelegramApiException e) {
                logger.severe("An error occurred while deleting a message: " + e.getMessage());
            }
        }, delay, TimeUnit.SECONDS);
        return message;
    }

    public Message sendMessage(Long chatId, String text) {
        return sendMessage(chatId, text, null);
    }

    public Message sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        var message = new SendMessage(chatId.toString(), text);
        if (keyboard == null) {
            var removeKeyboard = new ReplyKeyboardRemove();
            removeKeyboard.setRemoveKeyboard(true);
            message.setReplyMarkup(removeKeyboard);
        } else {
            message.setReplyMarkup(keyboard);
        }
        return sendMessage(message);
    }

    public Message sendMessage(SendMessage message) {
        try {
            return this.sendApiMethod(message);
        } catch (TelegramApiException e) {
            logger.severe("An error occurred while sending a message: " + e.getMessage());
        }
        return null;
    }

    private void setCommands(List<BotCommand> listOfCommands) {
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logger.severe("An error occurred while setting the commands: " + e.getMessage());
        }
    }
}