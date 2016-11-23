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

public class BanCommand extends BasicCommand {
    public BanCommand() {
        super("Ban");
        this.setDescription(this.getMessage("command_ban"));
        this.setUsage("/ch ban " + ChatColor.DARK_GRAY + "[channel] <player>");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch ban", "survivorbot ban");
        this.setNotes(
                ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, your active channel is used.");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Chatter chatter = null;
        if (sender instanceof Player) {
            Player targetName = (Player) sender;
            chatter = SurvivorBot.getChatterManager().getChatter(targetName);
        }

        Channel channel;
        if (args.length == 1) {
            if (chatter != null) {
                channel = chatter.getActiveChannel();
            } else {
                channel = SurvivorBot.getChannelManager().getDefaultChannel();
            }
        } else {
            channel = SurvivorBot.getChannelManager().getChannel(args[0]);
        }

        if (channel == null) {
            Messaging.send(sender, this.getMessage("ban_noChannel"));
            return true;
        } else if (chatter != null && chatter.canBan(channel) != Chatter.Result.ALLOWED) {
            Messaging.send(sender, this.getMessage("ban_noPermission"), channel.getColor() + channel.getName());
            return true;
        } else {
            String targetName1 = args[args.length - 1];
            Player targetPlayer = Bukkit.getServer().getPlayer(targetName1);
            if (targetPlayer != null) {
                targetName1 = targetPlayer.getName();
            }

            if (channel.isBanned(targetName1)) {
                channel.setBanned(targetName1, false);
                Messaging.send(sender, this.getMessage("ban_confirmUnban"), targetName1,
                        channel.getColor() + channel.getName());
                if (targetPlayer != null) {
                    Messaging.send(targetPlayer, this.getMessage("ban_notifyUnBan"),
                            channel.getColor() + channel.getName());
                }
            } else {
                if (targetPlayer != null) {
                    Chatter target = SurvivorBot.getChatterManager().getChatter(targetPlayer);
                    channel.banMember(target, true, true);
                    if (target.getChannels().isEmpty()) {
                        SurvivorBot.getChannelManager().getDefaultChannel().addMember(target, true, true);
                    }

                    if (channel.equals(target.getActiveChannel())) {
                        Channel focus = target.getChannels().iterator().next();
                        target.setActiveChannel(focus, true, true);
                    }
                } else {
                    channel.setBanned(targetName1, true);
                }

                Messaging.send(sender, this.getMessage("ban_confirmBan"), targetName1,
                        channel.getColor() + channel.getName());
            }

            return true;
        }
    }
}








