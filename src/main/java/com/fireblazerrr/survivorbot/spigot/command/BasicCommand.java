package com.fireblazerrr.survivorbot.spigot.command;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import org.bukkit.command.CommandSender;

public abstract class BasicCommand implements Command {
    private final String name;
    private String description = "";
    private String usage = "";
    private String permission = "";
    private String[] notes = new String[0];
    private String[] identifiers = new String[0];
    private int minArguments = 0;
    private int maxArguments = 0;

    public BasicCommand(String name) {
        this.name = name;
    }

    protected String getMessage(String key) {
        try {
            return SurvivorBot.getMessage(key);
        } catch (MessageNotFoundException ex) {
            SurvivorBot.severe("Message.properties is missing: " + key);
            return "";
        }
    }

    @Override
    public void cancelInteraction(CommandSender cs) {

    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String[] getIdentifiers() {
        return this.identifiers;
    }

    @Override
    public int getMaxArguments() {
        return this.maxArguments;
    }

    @Override
    public int getMinArguments() {
        return this.minArguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String[] getNotes() {
        return this.notes;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean isIdentifier(CommandSender cs, String s) {
        String[] args = this.identifiers;

        for (String identifier : args) {
            if (s.equalsIgnoreCase(identifier))
                return true;
        }

        return false;
    }

    @Override
    public boolean isInProgress(CommandSender cs) {
        return false;
    }

    @Override
    public boolean inInteractive() {
        return false;
    }

    @Override
    public boolean isShownOnHelpMenu() {
        return true;
    }

    public void setArgumentRange(int min, int max) {
        this.minArguments = min;
        this.maxArguments = max;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdentifiers(String... identifiers) {
        this.identifiers = identifiers;
    }

    public void setNotes(String... notes) {
        this.notes = notes;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
