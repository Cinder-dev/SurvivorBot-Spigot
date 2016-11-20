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

public class MuteCommand extends BasicCommand {
    public MuteCommand() {
        super("Mute");
        this.setDescription(this.getMessage("command_mute"));
        this.setUsage("/ch mute " + ChatColor.DARK_GRAY + "[channel] <player>");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch mute", "survivorbot mute");
        this.setNotes(ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, user is globally muted.");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Chatter chatter = null;
        if (sender instanceof Player) {
            Player targetName = (Player) sender;
            chatter = SurvivorBot.getChatterManager().getChatter(targetName);
        }

        String targetName1 = args[args.length - 1];
        Player targetPlayer = Bukkit.getServer().getPlayer(targetName1);
        if (targetPlayer != null) {
            targetName1 = targetPlayer.getName();
        }

        if (args.length == 2) {
            Channel channel = SurvivorBot.getChannelManager().getChannel(args[0]);
            if (channel == null) {
                Messaging.send(sender, this.getMessage("mute_noChannel"));
                return true;
            }

            if (chatter != null && chatter.canMute(channel) != Chatter.Result.ALLOWED) {
                Messaging.send(sender, this.getMessage("mute_noPermission"));
                return true;
            }

            if (channel.isMuted(targetName1)) {
                channel.setMuted(targetName1, false);
                Messaging.send(sender, this.getMessage("mute_confirmUnmute"), targetName1,
                        channel.getColor() + channel.getName());
                if (targetPlayer != null) {
                    Messaging.send(targetPlayer, this.getMessage("mute_notifyUnmute"),
                            channel.getColor() + channel.getName());
                }
            } else {
                channel.setMuted(targetName1, true);
                Messaging.send(sender, this.getMessage("mute_confirmMute"), targetName1,
                        channel.getColor() + channel.getName());
                if (targetPlayer != null) {
                    Messaging.send(targetPlayer, this.getMessage("mute_notifyMute"),
                            channel.getColor() + channel.getName());
                }
            }
        } else {
            if (chatter != null && !chatter.getPlayer().hasPermission("survivorbotchat.mute")) {
                Messaging.send(sender, this.getMessage("mute_noPermission"));
                return true;
            }

            if (targetPlayer == null) {
                Messaging.send(sender, this.getMessage("mute_noPlayer"));
                return true;
            }

            Chatter targetChatter = SurvivorBot.getChatterManager().getChatter(targetPlayer);
            if (targetChatter.isMuted()) {
                targetChatter.setMuted(false, true);
                Messaging.send(sender, this.getMessage("mute_confirmGlobalUnmute"), targetPlayer.getName());
                Messaging.send(targetPlayer, this.getMessage("mute_notifyGlobalUnmute"));
            } else {
                targetChatter.setMuted(true, true);
                Messaging.send(sender, this.getMessage("mute_confirmGlobalMute"), targetPlayer.getName());
                Messaging.send(targetPlayer, this.getMessage("mute_notifyGlobalMute"));
            }
        }

        return true;
    }
}
