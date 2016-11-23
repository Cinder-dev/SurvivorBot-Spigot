package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.channel.ConversationChannel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand extends BasicCommand {
    public ReplyCommand() {
        super("Reply");
        this.setDescription(this.getMessage("command_reply"));
        this.setUsage("/r " + ChatColor.DARK_GRAY + "[message]");
        this.setArgumentRange(0, 2147483647);
        this.setIdentifiers("reply", "r", "ch reply", "survivorbot reply");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            Chatter playerChatter = SurvivorBot.getChatterManager().getChatter(player);
            Chatter storedTargetChatter = playerChatter.getLastPrivateMessageSource();
            if (storedTargetChatter == null) {
                Messaging.send(sender, this.getMessage("reply_noMessages"));
                return true;
            } else {
                Player target = Bukkit.getPlayer(storedTargetChatter.getName());
                if (target == null) {
                    Messaging.send(sender, this.getMessage("reply_noPlayer"));
                    return true;
                } else if (target.equals(player)) {
                    Messaging.send(sender, this.getMessage("reply_selfReply"));
                    return true;
                } else {
                    Chatter targetChatter = SurvivorBot.getChatterManager().getChatter(target);
                    ChannelManager channelManager = SurvivorBot.getChannelManager();
                    String channelName = "convo" + player.getName() + target.getName();
                    if (!channelManager.hasChannel(channelName)) {
                        ConversationChannel convo = new ConversationChannel(playerChatter, targetChatter,
                                channelManager);
                        channelManager.addChannel(convo);
                    }

                    Channel var17 = channelManager.getChannel(channelName);
                    if (args.length == 0) {
                        playerChatter.setActiveChannel(var17, false, true);
                        Messaging.send(player, this.getMessage("reply_confirm"), target.getName());
                    } else {
                        StringBuilder msg = new StringBuilder();

                        Arrays.stream(args).forEach(s -> msg.append(s).append(" "));

                        Channel var18 = playerChatter.getActiveChannel();
                        playerChatter.setActiveChannel(var17, false, false);
                        SurvivorBot.getMessageHandler().handle(player, msg.toString().trim(), "<%1$s> %2$s");
                        playerChatter.setActiveChannel(var18, false, false);
                    }

                    return true;
                }
            }
        }
    }
}
