package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.manager.FriendManager;

public class FriendCommand
        extends Command {
    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            if (uop.friendManager.getFriends().isEmpty()) {
                FriendCommand.sendMessage("Friend list empty D:.");
            } else {
                String f = "Friends: ";
                for (FriendManager.Friend friend : uop.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    } catch (Exception exception) {
                    }
                }
                FriendCommand.sendMessage(f);
            }
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    uop.friendManager.onLoad();
                    FriendCommand.sendMessage("Friends got reset.");
                    return;
                }
            }
            FriendCommand.sendMessage(commands[0] + (uop.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            return;
        }
        if (commands.length >= 2) {
            switch (commands[0]) {
                case "add": {
                    uop.friendManager.addFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    return;
                }
                case "del": {
                    uop.friendManager.removeFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    return;
                }
            }
            FriendCommand.sendMessage("Unknown Command, try friend add/del (name)");
        }
    }
}

