package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand extends BasicCommand {
    public InfoCommand() {
        super("Info");
        this.setDescription(this.getMessage("command_info"));
        this.setUsage("/ch info " + ChatColor.DARK_GRAY + "[channel]");
        this.setArgumentRange(0, 1);
        this.setIdentifiers("ch info", "survivorbot info");
        this.setNotes(
                ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, your active channel is used.");

    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Chatter chatter = null;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            chatter = SurvivorBot.getChatterManager().getChatter(player);
        }

        Channel channel;
        if (args.length == 0) {
            if (chatter != null) {
                channel = chatter.getActiveChannel();
            } else {
                channel = SurvivorBot.getChannelManager().getDefaultChannel();
            }
        } else {
            channel = SurvivorBot.getChannelManager().getChannel(args[0]);
        }

        if (channel == null) {
            Messaging.send(sender, this.getMessage("info_noChannel"));
            return true;
        } else if (chatter != null && chatter.canViewInfo(channel) != Chatter.Result.ALLOWED) {
            Messaging.send(sender, this.getMessage("info_noPermission"), channel.getColor() + channel.getName());
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "------------[ " + channel.getColor() + channel
                    .getName() + ChatColor.RED + " ]------------");
            sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.WHITE + channel.getName());
            sender.sendMessage(ChatColor.YELLOW + "Nick: " + ChatColor.WHITE + channel.getNick());
            sender.sendMessage(ChatColor.YELLOW + "Format: " + ChatColor.WHITE + channel.getFormat());
            if (!channel.getPassword().isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + "Password: " + ChatColor.WHITE + channel.getPassword());
            }

            if (channel.getDistance() > 0) {
                sender.sendMessage(ChatColor.YELLOW + "Distance: " + ChatColor.WHITE + channel.getDistance());
            }

            sender.sendMessage(
                    ChatColor.YELLOW + "Shortcut Allowed: " + ChatColor.WHITE + (channel.isShortcutAllowed() ? "true" :
                            "false"));
            return true;
        }
    }
}
