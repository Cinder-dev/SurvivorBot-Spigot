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

public class RemoveCommand extends BasicCommand {
    public RemoveCommand() {
        super("Remove Channel");
        this.setDescription(this.getMessage("command_remove"));
        this.setUsage("/ch remove " + ChatColor.DARK_GRAY + "<channel>");
        this.setArgumentRange(1, 1);
        this.setIdentifiers("ch remove", "survivorbot remove");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        String name = args[0];
        ChannelManager channelMngr = SurvivorBot.getChannelManager();
        Channel channel = channelMngr.getChannel(name);
        if (channel == null) {
            Messaging.send(sender, this.getMessage("remove_noChannel"));
            return true;
        } else {
            Chatter target;
            if (sender instanceof Player) {
                Player i$ = (Player) sender;
                target = SurvivorBot.getChatterManager().getChatter(i$);
                if (target.canRemove(channel) != Chatter.Result.ALLOWED) {
                    Messaging.send(sender, this.getMessage("remove_noPermission"),
                            channel.getColor() + channel.getName());
                    return true;
                }
            }

            for (Chatter o : channel.getMembers()) {
                target = o;
                channel.kickMember(target, true);
                if (target.getChannels().isEmpty()) {
                    SurvivorBot.getChannelManager().getDefaultChannel().addMember(target, true, true);
                }

                if (channel.equals(target.getActiveChannel())) {
                    Channel focus = target.getChannels().iterator().next();
                    target.setActiveChannel(focus, true, true);
                }
            }

            channelMngr.removeChannel(channel);
            Messaging.send(sender, this.getMessage("remove_confirm"));
            return true;
        }
    }
}
