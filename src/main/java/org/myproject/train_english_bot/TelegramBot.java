package org.myproject.train_english_bot;

import org.myproject.train_english_bot.models.Mode;
import org.myproject.train_english_bot.service.CommandService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private static final Logger logger = Logger.getLogger(TelegramBot.class.getName());

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
            Mode mode = Mode.DEFAULT; // Заменить на взятия из БД

            if (mode == Mode.TRAIN) {

            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void sendMessage(Long chatId, String text) {
        try {
            sendApiMethod(new SendMessage(chatId.toString(), text));
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
