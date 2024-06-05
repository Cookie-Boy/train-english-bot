package org.myproject.train_english_bot.models;

import jakarta.persistence.Embeddable;

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
}
