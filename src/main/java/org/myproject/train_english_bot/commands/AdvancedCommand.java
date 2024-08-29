package org.myproject.train_english_bot.commands;

import org.myproject.train_english_bot.models.User;

import java.util.List;

public abstract class AdvancedCommand extends Command {
    public abstract void execute(User user, List<String> args);
}
