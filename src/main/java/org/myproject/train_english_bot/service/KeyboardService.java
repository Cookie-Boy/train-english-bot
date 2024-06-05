package org.myproject.train_english_bot.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KeyboardService {
    public static ReplyKeyboardMarkup getDefaultKeyboard() {
        var keyboardRows = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        row.addAll(Arrays.asList("Training", "New words"));
        keyboardRows.add(row);
        return createKeyboardMarkup(keyboardRows);
    }

    public static ReplyKeyboardMarkup getAddWordKeyboard() {
        var keyboardRows = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        row.add("Stop adding new words");
        keyboardRows.add(row);
        return createKeyboardMarkup(keyboardRows);
    }

    public static ReplyKeyboardMarkup getTrainingKeyboard(@NotNull List<String> buttonNames) {
        var keyboardRows = new ArrayList<KeyboardRow>();
        for (int i = 0; i < buttonNames.size(); i += 2) {
            var row = new KeyboardRow();
            row.addAll(Arrays.asList(buttonNames.get(i), buttonNames.get(i + 1)));
            keyboardRows.add(row);
        }
        return createKeyboardMarkup(keyboardRows);
    }

    private static ReplyKeyboardMarkup createKeyboardMarkup(List<KeyboardRow> rows) {
        var keyboardMarkup = new ReplyKeyboardMarkup(rows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
