package org.myproject.train_english_bot.models;

import jakarta.persistence.Embeddable;

import java.util.Objects;

// Помечаем, что класс Word не является сущностью (для него не нужно создавать отдельную таблицу)
@Embeddable
public class Word {
    private String enVersion;
    private String ruVersion;
    private byte level;
    private boolean isAvailable;

    public Word(String enVersion, String ruVersion, byte level, boolean available) {
        this.enVersion = enVersion;
        this.ruVersion = ruVersion;
        this.level = level;
        this.isAvailable = available;
    }

    public Word(String enVersion, String ruVersion) {
        this.enVersion = enVersion;
        this.ruVersion = ruVersion;
        this.level = 0;
        this.isAvailable = true;
    }

    public Word() {

    }

    public String getEnVersion() {
        return enVersion;
    }

    public void setEnVersion(String enVersion) {
        this.enVersion = enVersion;
    }

    public String getRuVersion() {
        return ruVersion;
    }

    public void setRuVersion(String ruVersion) {
        this.ruVersion = ruVersion;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return level == word.level && isAvailable == word.isAvailable && Objects.equals(enVersion, word.enVersion) && Objects.equals(ruVersion, word.ruVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enVersion, ruVersion, level, isAvailable);
    }
}
