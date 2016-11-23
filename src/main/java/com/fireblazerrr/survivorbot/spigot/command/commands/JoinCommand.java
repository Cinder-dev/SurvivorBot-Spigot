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

public class JoinCommand extends BasicCommand {
    public JoinCommand() {
        super("Join");
        this.setDescription(this.getMessage("command_join"));
        this.setUsage("/ch join " + ChatColor.DARK_GRAY + "<channel> [password]");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("join", "ch join", "survivorbot join");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            ChannelManager channelMngr = SurvivorBot.getChannelManager();
            Channel channel = channelMngr.getChannel(args[0]);
            if (channel == null) {
                Messaging.send(sender, this.getMessage("join_noChannel"));
                return true;
            } else {
                String password = "";
                if (args.length == 2) {
                    password = args[1];
                }

                Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
                Chatter.Result result = chatter.canJoin(channel, password);
                switch (result.ordinal()) {
                    case 2:
                        Messaging.send(sender, this.getMessage("join_redundant"),
                                channel.getColor() + channel.getName());
                        return true;
                    case 0:
                        Messaging.send(sender, this.getMessage("join_noPermission"),
                                channel.getColor() + channel.getName());
                        return true;
                    case 3:
                        Messaging.send(sender, this.getMessage("join_banned"), channel.getColor() + channel.getName());
                        return true;
                    case 7:
                        Messaging.send(sender, this.getMessage("join_badPassword"));
                        return true;
                    default:
                        channel.addMember(chatter, true, true);
                        SurvivorBot.getChatterManager().getChatter(player).setActiveChannel(channel, false, false);
                        Messaging.send(player, this.getMessage("join_confirm"), channel.getColor() + channel.getName());
                        return true;
                }
            }
        }
    }
}
