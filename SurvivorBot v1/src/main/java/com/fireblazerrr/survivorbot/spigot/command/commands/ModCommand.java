package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModCommand extends BasicCommand {
    public ModCommand() {
        super("Mod");
        this.setDescription(this.getMessage("command_mod"));
        this.setUsage("/ch mod " + ChatColor.DARK_GRAY + "[channel] <player>");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch mod", "survivorbot mod");
        this.setNotes(
                ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, your active channel is used.");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Channel channel = null;
        Chatter chatter = null;
        if (sender instanceof Player) {
            Player targetName = (Player) sender;
            chatter = SurvivorBot.getChatterManager().getChatter(targetName);
            channel = chatter.getActiveChannel();
        }

        if (args.length == 1) {
            if (chatter != null) {
                channel = chatter.getActiveChannel();
            }
        } else {
            channel = SurvivorBot.getChannelManager().getChannel(args[0]);
        }

        if (channel == null) {
            Messaging.send(sender, this.getMessage("mod_noChannel"));
            return true;
        } else if (!sender.hasPermission("survivorbot.mod") && (chatter == null || !channel
                .isModerator(chatter.getPlayer().getName()))) {
            Messaging.send(sender, this.getMessage("mod_noPermission"), channel.getColor() + channel.getName());
            return true;
        } else {
            String targetName1 = args[args.length - 1];
            Player targetPlayer = Bukkit.getServer().getPlayer(targetName1);
            if (channel.isModerator(targetName1)) {
                channel.setModerator(targetName1, false);
                Messaging.send(sender, this.getMessage("mod_confirmUnmod"), channel.getColor() + channel.getName(),
                        targetName1);
                if (targetPlayer != null) {
                    Messaging.send(targetPlayer, this.getMessage("mod_notifyUnmod"),
                            channel.getColor() + channel.getName());
                }
            } else {
                channel.setModerator(targetName1, true);
                Messaging.send(sender, this.getMessage("mod_confirmMod"), channel.getColor() + channel.getName(),
                        targetName1);
                if (targetPlayer != null) {
                    Messaging.send(targetPlayer, this.getMessage("mod_notifyMod"),
                            channel.getColor() + channel.getName());
                }
            }

            return true;
        }
    }
}
