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

public class MsgCommand extends BasicCommand {
    public MsgCommand() {
        super("Private Message");
        this.setDescription(this.getMessage("command_msg"));
        this.setUsage("/msg " + ChatColor.DARK_GRAY + "<player> [message]");
        this.setArgumentRange(0, 2147483647);
        this.setPermission("survivorbot.pm");
        this.setIdentifiers("msg", "w", "tell", "pm", "ch msg", "survivorbot msg");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Player player;
        if (!(sender instanceof Player)) {
            if (args.length < 2) {
                Messaging.send(sender, this.getMessage("console_noMessage"));
                return true;
            } else {
                player = Bukkit.getServer().getPlayer(args[0]);
                if (player == null) {
                    Messaging.send(sender, this.getMessage("msg_noPlayer"));
                    return true;
                } else {
                    StringBuilder var13 = new StringBuilder();

                    for (int var15 = 1; var15 < args.length; ++var15) {
                        var13.append(args[var15]).append(" ");
                    }

                    Messaging.send(player, ChatColor.GREEN + "<Console> --->" + ChatColor.WHITE + "  $1",
                            var13.toString().trim());
                    Messaging.send(sender, "----> $1: $2", player.getName(), var13.toString().trim());
                    return true;
                }
            }
        } else {
            player = (Player) sender;
            Chatter playerChatter = SurvivorBot.getChatterManager().getChatter(player);
            if (args.length == 0) {
                Channel var14 = playerChatter.getActiveChannel();
                if (var14 != null && var14.isTransient()) {
                    Channel var16 = playerChatter.getLastFocusableChannel();
                    if (var16 != null) {
                        playerChatter.setActiveChannel(var16, true, true);
                    }
                }

                return true;
            } else {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target != null && player.canSee(target)) {
                    if (target.equals(player)) {
                        Messaging.send(sender, this.getMessage("msg_selfMsg"));
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
                        if (args.length == 1) {
                            playerChatter.setActiveChannel(var17, false, false);
                            Messaging.send(player, this.getMessage("msg_confirm"), target.getName());
                        } else {
                            StringBuilder msg = new StringBuilder();

                            for (int active = 1; active < args.length; ++active) {
                                msg.append(args[active]).append(" ");
                            }

                            Channel var18 = playerChatter.getActiveChannel();
                            playerChatter.setActiveChannel(var17, false, false);
                            SurvivorBot.getMessageHandler().handle(player, msg.toString().trim(), "<%1$s> %2$s");
                            playerChatter.setActiveChannel(var18, false, false);
                        }

                        return true;
                    }
                } else {
                    Messaging.send(sender, this.getMessage("msg_noPlayer"));
                    return true;
                }
            }
        }
    }
}
