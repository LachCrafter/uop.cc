package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : uop.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + uop.commandManager.getPrefix() + command.getName());
        }
    }
}

