package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.spigot.events.ChannelChatEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageHandler;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EmoteCommand extends BasicCommand {
    public EmoteCommand() {
        super("Emote");
        this.setDescription(this.getMessage("command_emote"));
        this.setUsage("/me " + ChatColor.DARK_GRAY + "[message]");
        this.setArgumentRange(1, 2147483647);
        this.setIdentifiers("me");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        StringBuilder msg = new StringBuilder();
        if (sender instanceof Player) {
            msg.append(((Player) sender).getDisplayName());
        } else {
            msg.append(sender.getName());
        }

        Arrays.stream(args).forEach(s -> msg.append(" ").append(s));

        if (SurvivorBot.getChannelManager().isUsingEmotes() && sender instanceof Player) {
            Player player = (Player) sender;
            Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
            Channel channel = chatter.getActiveChannel();
            Chatter.Result result = chatter.canEmote(channel);
            ChannelChatEvent channelChatEvent = MessageHandler
                    .throwChannelEvent(chatter, channel, result, msg.toString(), "",
                            channel.getFormatSupplier().getEmoteFormat());
            result = channelChatEvent.getResult();
            switch (result.ordinal()) {
                case 1:
                    try {
                        Messaging.send(player, SurvivorBot.getMessage("messageHandler_noChannel"));
                    } catch (MessageNotFoundException e) {
                        SurvivorBot.severe("Messages.properties is missing: messageHandler_noChannel");
                    }
                    break;
                case 2:
                    try {
                        Messaging.send(player, SurvivorBot.getMessage("messageHandler_notInChannel"));
                    } catch (MessageNotFoundException var14) {
                        SurvivorBot.severe("Messages.properties is missing: messageHandler_notInChannel");
                    }
                    break;
                case 4:
                    try {
                        Messaging.send(player, SurvivorBot.getMessage("messageHandler_muted"));
                    } catch (MessageNotFoundException var13) {
                        SurvivorBot.severe("Messages.properties is missing: messageHandler_muted");
                    }
                    break;
                case 0:
                    try {
                        Messaging.send(player, SurvivorBot.getMessage("messageHandler_noPermission"),
                                channel.getColor() + channel.getName());
                    } catch (MessageNotFoundException var12) {
                        SurvivorBot.severe("Messages.properties is missing: messageHandler_noPermission");
                    }
                    break;
                case 6:
                    try {
                        Messaging.send(player, SurvivorBot.getMessage("messageHandler_badWorld"),
                                channel.getColor() + channel.getName());
                    } catch (MessageNotFoundException var11) {
                        SurvivorBot.severe("Messages.properties is missing: messageHandler_badWorld");
                    }
                    break;
            }

            if (result != Chatter.Result.ALLOWED) {
                return true;
            } else {
                channel.emote(chatter, msg.toString());
                if (!channel.getDiscordChannelLinkID().equals("")) {
                    SurvivorBot.getInstance().getDJA().getTextChannelById(channel.getDiscordChannelLinkID()).sendMessage(msg.toString());
                }
                return true;
            }
        } else {
            if (sender.hasPermission("survivorbot.emote")) {
                Bukkit.broadcastMessage("* " + msg);
            } else {
                Messaging.send(sender, this.getMessage("emote_noPermission"));
            }

            return true;
        }
    }
}
