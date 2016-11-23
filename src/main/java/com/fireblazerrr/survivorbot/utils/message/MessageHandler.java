package com.fireblazerrr.survivorbot.utils.message;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.chatter.ChatterManager;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {
    private List<String> censors = new ArrayList<>();
    private boolean twitterStyleMsgs = true;

    public MessageHandler() {
    }

    public void setTwitterStyleMsgs(boolean twitterStyleMsgs) {
        this.twitterStyleMsgs = twitterStyleMsgs;
    }

    private String censor(String msg) {
        final String[] test = new String[1];
        test[0] = msg;
        censors.forEach(s -> {
            String[] split = s.split(";", 2);
            if (split.length == 1)
                test[0] = this.censor(test[0], s, false, "");
            else
                test[0] = this.censor(test[0], split[0], true, split[1]);
        });

        return test[0];
    }

    private String censor(String msg, String censor, boolean customReplacement, String replacement) {
        Pattern pattern = Pattern.compile(censor, 2);
        Matcher matcher = pattern.matcher(msg);

        StringBuilder censoredMsg;
        for (censoredMsg = new StringBuilder(); matcher.find(); matcher = pattern.matcher(msg)) {
            String match = matcher.group();
            if (!customReplacement) {
                char[] replaceChars = new char[match.length()];
                Arrays.fill(replaceChars, '*');
                replacement = new String(replaceChars);
            }

            censoredMsg.append(msg.substring(0, matcher.start())).append(replacement);
            msg = msg.substring(matcher.end());
        }

        censoredMsg.append(msg);
        return censoredMsg.toString();
    }

    public void handle(Player player, String msg, String format) {
        if (player.isOnline() && player != null) {
            if (this.twitterStyleMsgs && msg.startsWith("@") && msg.length() > 1 && msg.charAt(1) != 32) {
                msg = "msg " + msg.substring(1);
                SurvivorBot.getCommandHandler().dispatch(player, "ch", msg.split(" "));
            } else {
                ChatterManager chatterManager = SurvivorBot.getChatterManager();
                if (!chatterManager.hasChatter(player)) {
                    chatterManager.addChatter(player);
                }

                Chatter sender = chatterManager.getChatter(player);
                if (sender == null) {
                    throw new RuntimeException("Chatter {" + player.getName() + "} not found.");
                } else {
                    Chatter.Result result = null;
                    Channel channel = sender.getActiveChannel();
                    ChannelChatEvent channelChatEvent = null;
                    if (channel == null) {
                        result = Chatter.Result.NO_CHANNEL;
                    } else {
                        result = sender.canSpeak(channel);
                        channelChatEvent = throwChannelEvent(sender, channel, result, msg, format, channel.getFormat());
                        result = channelChatEvent.getResult();
                        channel = channelChatEvent.getChannel();
                    }

                    switch (result.ordinal()) {
                        case 1:
                            try {
                                Messaging.send(player, SurvivorBot.getMessage("messageHandler_noChannel"));
                            } catch (MessageNotFoundException ignored) {
                                SurvivorBot.severe("Messages.properties is missing: messageHandler_noChannel");
                            }
                            break;
                        case 2:
                            try {
                                Messaging.send(player, SurvivorBot.getMessage("messageHandler_notInChannel"));
                            } catch (MessageNotFoundException ignored) {
                                SurvivorBot.severe("Messages.properties is missing: messageHandler_notInChannel");
                            }
                            break;
                        case 4:
                            try {
                                Messaging.send(player, SurvivorBot.getMessage("messageHandler_muted"));
                            } catch (MessageNotFoundException ignored) {
                                SurvivorBot.severe("Messages.properties is missing: messageHandler_muted");
                            }
                            break;
                        case 0:
                            try {
                                Messaging.send(player, SurvivorBot.getMessage("messageHandler_noPermission"),
                                        channel.getColor() + channel.getName());
                            } catch (MessageNotFoundException ignored) {
                                SurvivorBot.severe("Messages.properties is missing: messageHandler_noPermission");
                            }
                            break;
                        case 6:
                            try {
                                Messaging.send(player, SurvivorBot.getMessage("messageHandler_badWorld"),
                                        channel.getColor() + channel.getName());
                            } catch (MessageNotFoundException ignored) {
                                SurvivorBot.severe("Messages.properties is missing: messageHandler_badWorld");
                            }
                            break;
                    }

                    if (result == Chatter.Result.ALLOWED) {
                        Pattern pattern = Pattern.compile("(?i)(&)([0-9a-fk-or])");
                        Matcher match = pattern.matcher(channelChatEvent.getMessage());
                        StringBuffer sb = new StringBuffer();

                        while (match.find()) {
                            ChatColor color = ChatColor.getByChar(match.group(2).toLowerCase());
                            if (sender.canColorMessage(channel, color) == Chatter.Result.ALLOWED) {
                                match.appendReplacement(sb, color.toString());
                            } else {
                                match.appendReplacement(sb, "");
                            }
                        }

                        channelChatEvent.setMessage(this.censor(match.appendTail(sb).toString()));
                        channel.processChat(channelChatEvent);
                    }
                }
            }
        }
    }

    public static ChannelChatEvent throwChannelEvent(Chatter sender, Channel channel, Chatter.Result result, String msg,
                                                     String format, String format1) {

        ChannelChatEvent event = new ChannelChatEvent(sender, channel, result, msg, format, format1);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public void setCensors(List<String> censors) {
        this.censors = censors;
    }
}















