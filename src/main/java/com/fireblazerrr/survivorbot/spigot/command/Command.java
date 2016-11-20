package com.fireblazerrr.survivorbot.spigot.command;

import org.bukkit.command.CommandSender;

public interface Command {

    void cancelInteraction(CommandSender cs);

    boolean execute(CommandSender sender, String identifier, String[] args);

    String getDescription();

    String[] getIdentifiers();

    int getMaxArguments();

    int getMinArguments();

    String getName();

    String[] getNotes();

    String getPermission();

    String getUsage();

    boolean isIdentifier(CommandSender cs, String s);

    boolean isInProgress(CommandSender cs);

    boolean inInteractive();

    boolean isShownOnHelpMenu();
}
