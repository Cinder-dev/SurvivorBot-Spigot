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

public class KickCommand extends BasicCommand {
    public KickCommand() {
        super("Kick");
        this.setDescription(this.getMessage("command_kick"));
        this.setUsage("/ch kick " + ChatColor.DARK_GRAY + "[channel] <player>");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch kick", "survivorbot kick");
        this.setNotes(
                ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, your active channel is used.");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Channel channel = null;
        Chatter chatter = null;
        Player targetPlayer;
        if (sender instanceof Player) {
            targetPlayer = (Player) sender;
            chatter = SurvivorBot.getChatterManager().getChatter(targetPlayer);
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
            Messaging.send(sender, this.getMessage("kick_noChannel"));
            return true;
        } else if (chatter != null && chatter.canKick(channel) != Chatter.Result.ALLOWED) {
            Messaging.send(sender, this.getMessage("kick_noPermission"), channel.getColor() + channel.getName());
            return true;
        } else {
            targetPlayer = Bukkit.getServer().getPlayer(args[args.length - 1]);
            if (targetPlayer == null) {
                Messaging.send(sender, this.getMessage("kick_noPlayer"));
                return true;
            } else {
                Chatter target = SurvivorBot.getChatterManager().getChatter(targetPlayer);
                if (!target.hasChannel(channel)) {
                    Messaging.send(sender, this.getMessage("kick_badPlayer"));
                    return true;
                } else {
                    channel.kickMember(target, true);
                    Messaging.send(sender, this.getMessage("kick_confirm"), targetPlayer.getName(),
                            channel.getColor() + channel.getName());
                    Messaging
                            .send(targetPlayer, this.getMessage("kick_notify"), channel.getColor() + channel.getName());
                    if (target.getChannels().isEmpty()) {
                        SurvivorBot.getChannelManager().getDefaultChannel().addMember(target, true, true);
                    }

                    if (channel.equals(target.getActiveChannel())) {
                        Channel focus = target.getChannels().iterator().next();
                        target.setActiveChannel(focus, true, true);
                    }

                    return true;
                }
            }
        }
    }
}
