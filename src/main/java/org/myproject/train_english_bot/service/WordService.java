package org.myproject.train_english_bot.service;

import org.myproject.train_english_bot.models.LanguageType;
import org.myproject.train_english_bot.models.Word;

public class AddingService {
    public static Word getLearningWord(String text) {
        String[] pair = text.toLowerCase().split(" ");
        if (pair.length != 2) {
            return null;
        }
        LanguageType wordLang1 = getWordLanguage(pair[0]);
        if (wordLang1 == LanguageType.UNKNOWN) {
            return null;
        }
        LanguageType wordLang2 = getWordLanguage(pair[1]);
        if (wordLang2 == LanguageType.UNKNOWN) {
            return null;
        }
        if (wordLang1 == wordLang2) {
            return null;
        }

        if (wordLang1 == LanguageType.ENGLISH) {
            return new Word(pair[0], pair[1]);
        }
        return new Word(pair[1], pair[0]);
    }

    private static LanguageType getWordLanguage(String word) {
        LanguageType result = LanguageType.UNKNOWN;
        for (int i = 0; i < word.length(); i++) {
            char symbol = word.charAt(i);
            LanguageType symbolLang = getSymbolLanguage(symbol);
            boolean isHyphen = symbol == '-' || symbol == '–' || symbol == '—';

            if (symbolLang == LanguageType.UNKNOWN) {
                if (!isHyphen)
                    return LanguageType.UNKNOWN;
                continue;
            }
            if (result == LanguageType.UNKNOWN) {
                result = symbolLang;
                continue;
            }
            if (symbolLang != result) {
                return LanguageType.UNKNOWN;
            }
        }
        return result;
    }

    private static LanguageType getSymbolLanguage(char symbol) {
        if ((symbol >= 'A' && symbol <= 'Z') || (symbol >= 'a' && symbol <= 'z')) {
            return LanguageType.ENGLISH;
        }
        if ((symbol >= 'А' && symbol <= 'Я') || (symbol >= 'а' && symbol <= 'я')) {
            return LanguageType.RUSSIAN;
        }
        return LanguageType.UNKNOWN;
    }
}
