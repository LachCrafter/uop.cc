package me.alpha432.oyvey.features.command.commands;

import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        uop.reload();
    }
}

