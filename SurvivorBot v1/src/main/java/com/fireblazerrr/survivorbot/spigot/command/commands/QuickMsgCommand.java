package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuickMsgCommand extends BasicCommand {
    public QuickMsgCommand() {
        super("Quick Message");
        this.setDescription(this.getMessage("command_qmsg"));
        this.setUsage("/ch qm " + ChatColor.DARK_GRAY + "<channel> <message>");
        this.setArgumentRange(2, 2147483647);
        this.setIdentifiers("ch qm", "survivorbot qm");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            Channel var9 = SurvivorBot.getChannelManager().getChannel(args[0]);
            if (var9 == null) {
                Messaging.send(sender, this.getMessage("quickmsg_noChannel"));
                return true;
            } else {
                StringBuilder var10 = new StringBuilder();

                for (int var11 = 1; var11 < args.length; ++var11) {
                    var10.append(args[var11]).append(" ");
                }

                var9.announce(var10.toString());
                sender.sendMessage("Announcement sent to " + var9.getName());
                return true;
            }
        } else {
            Player player = (Player) sender;
            Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
            Channel channel = SurvivorBot.getChannelManager().getChannel(args[0]);
            if (channel == null) {
                Messaging.send(sender, this.getMessage("quickmsg_noChannel"));
                return true;
            } else {
                StringBuilder msg = new StringBuilder();

                for (int active = 1; active < args.length; ++active) {
                    msg.append(args[active]).append(" ");
                }

                Channel var12 = chatter.getActiveChannel();
                chatter.setActiveChannel(channel, false, false);
                SurvivorBot.getMessageHandler().handle(player, msg.toString().trim(), "<%1$s> %2$s");
                chatter.setActiveChannel(var12, false, false);
                return true;
            }
        }
    }
}
