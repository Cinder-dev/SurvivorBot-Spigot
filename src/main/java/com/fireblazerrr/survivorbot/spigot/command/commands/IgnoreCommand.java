package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand extends BasicCommand {
    public IgnoreCommand() {
        super("Ignore");
        this.setDescription(this.getMessage("command_ignore"));
        this.setUsage("/ch ignore " + ChatColor.DARK_GRAY + "[player]");
        this.setArgumentRange(0, 1);
        this.setIdentifiers("ignore", "ch ignore", "survivorbot ignore");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
            if (args.length == 0) {
                StringBuilder targetName = new StringBuilder(this.getMessage("ignore_listHead"));
                if (chatter.getIgnores().isEmpty()) {
                    targetName.append(" ").append(this.getMessage("ignore_listEmpty"));
                } else {
                    for (Object name : chatter.getIgnores()) {
                        targetName.append(" ").append(name);
                    }
                }

                Messaging.send(sender, targetName.toString());
            } else {
                String targetName1 = args[args.length - 1];
                OfflinePlayer targetPlayer1 = Bukkit.getServer().getOfflinePlayer(targetName1);
                if (targetPlayer1 == null) {
                    Messaging.send(sender, this.getMessage("ignore_noPlayer"), targetName1);
                    return true;
                }

                if (chatter.isIgnoring(targetName1)) {
                    chatter.setIgnore(targetName1, false, true);
                    Messaging.send(sender, this.getMessage("ignore_confirmUnignore"), targetName1);
                } else {
                    chatter.setIgnore(targetName1, true, true);
                    Messaging.send(sender, this.getMessage("ignore_confirmIgnore"), targetName1);
                }
            }

            return true;
        }
    }
}
