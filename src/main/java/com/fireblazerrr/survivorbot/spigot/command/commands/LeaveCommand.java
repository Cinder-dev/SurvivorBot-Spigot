package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BasicCommand {
    public LeaveCommand() {
        super("Leave");
        this.setDescription(this.getMessage("command_leave"));
        this.setUsage("/ch leave " + ChatColor.DARK_GRAY + "<channel>");
        this.setArgumentRange(1, 1);
        this.setIdentifiers("leave", "ch leave", "survivorbot leave");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            ChannelManager channelMngr = SurvivorBot.getChannelManager();
            Channel channel = channelMngr.getChannel(args[0]);
            if (channel == null) {
                Messaging.send(sender, this.getMessage("leave_noChannel"));
                return true;
            } else {
                Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
                Chatter.Result result = chatter.canLeave(channel);
                switch (result.ordinal()) {
                    case 2:
                        Messaging.send(sender, this.getMessage("leave_badChannel"),
                                channel.getColor() + channel.getName());
                        return true;
                    case 0:
                        Messaging.send(sender, this.getMessage("leave_noPermission"),
                                channel.getColor() + channel.getName());
                        return true;
                    default:
                        int channelCount = chatter.getChannels().size();
                        if (channelCount == 1) {
                            Messaging.send(sender, this.getMessage("leave_lastChannel"));
                            return true;
                        } else {
                            channel.removeMember(chatter, true, true);
                            Messaging.send(player, this.getMessage("leave_confirm"),
                                    channel.getColor() + channel.getName());
                            if (chatter.getActiveChannel() == null || chatter.getActiveChannel().equals(channel)) {
                                Channel newFocus = chatter.getLastFocusableChannel();
                                if (newFocus == null || newFocus.equals(channel)) {
                                    newFocus = channelMngr.getDefaultChannel();
                                }

                                chatter.setActiveChannel(newFocus, true, true);
                            }

                            return true;
                        }
                }
            }
        }
    }
}
