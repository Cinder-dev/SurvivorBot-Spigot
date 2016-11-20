package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.command.CommandSender;

public class SaveCommand extends BasicCommand {
    public SaveCommand() {
        super("Save");
        this.setDescription(this.getMessage("command_save"));
        this.setUsage("/ch save");
        this.setArgumentRange(0, 0);
        this.setIdentifiers("ch save", "survivorbot save");
        this.setPermission("survivorbot.save");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        SurvivorBot.getChannelManager().getStorage().update();
        SurvivorBot.getChatterManager().getStorage().update();
        Messaging.send(sender, this.getMessage("save_confirm"));
        return true;
    }
}
