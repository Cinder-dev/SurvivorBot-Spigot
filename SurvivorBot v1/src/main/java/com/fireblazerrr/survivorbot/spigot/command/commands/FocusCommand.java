package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.channel.ConversationChannel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FocusCommand extends BasicCommand {
    public FocusCommand() {
        super("Focus");
        this.setDescription(this.getMessage("command_focus"));
        this.setUsage("/ch " + ChatColor.DARK_GRAY + "<channel> [password]");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch", "survivorbot");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            ChannelManager channelManager = SurvivorBot.getChannelManager();
            Channel channel = channelManager.getChannel(args[0]);
            if (channel == null) {
                Messaging.send(sender, this.getMessage("focus_noChannel"));
                return true;
            } else {
                String password = "";
                if (args.length == 2) {
                    password = args[1];
                }

                Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
                if (chatter.canFocus(channel) == Chatter.Result.NO_PERMISSION) {
                    Messaging.send(sender, this.getMessage("focus_noPermission"),
                            channel.getColor() + channel.getName());
                    return true;
                } else {
                    if (!chatter.hasChannel(channel)) {
                        Chatter.Result lastChannel = chatter.canJoin(channel, password);
                        switch (lastChannel.ordinal()) {
                            case 0:
                                Messaging.send(sender, this.getMessage("join_noPermission"),
                                        channel.getColor() + channel.getName());
                                return true;
                            case 3:
                                Messaging.send(sender, this.getMessage("focus_banned"),
                                        channel.getColor() + channel.getName());
                                return true;
                            case 7:
                                Messaging.send(sender, this.getMessage("focus_badPassword"));
                                return true;
                            default:
                                channel.addMember(chatter, true, true);
                                Messaging.send(player, this.getMessage("focus_confirm"),
                                        channel.getColor() + channel.getName());
                        }
                    }

                    chatter.setActiveChannel(channel, true, true);
                    Channel lastChannel1 = chatter.getLastActiveChannel();
                    if (lastChannel1 instanceof ConversationChannel) {
                        lastChannel1.getMembers().stream()
                                .filter(chatter1 -> !chatter1.getActiveChannel().equals(lastChannel1))
                                .forEach(chatter1 -> {
                                    lastChannel1.removeMember(chatter, false, true);
                                    lastChannel1.removeMember(chatter1, false, true);
                                });
                    }

                    return true;
                }
            }
        }
    }
}

