package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.discord.MessageListener;
import com.fireblazerrr.survivorbot.utils.Announcement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import redis.clients.jedis.JedisPubSub;

import java.util.stream.Collectors;

public class JedisListener extends JedisPubSub {

    public void onMessage(String ch, String msg) {
        SurvivorBot.info("JedisListener onMessage channel: " + ch + ", message: " + msg);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonData = jsonParser.parse(msg).getAsJsonObject();
        String id = jsonData.get("id").getAsString();
        String channel = jsonData.get("channel").getAsString().toLowerCase();
        String user;
        String message;

        switch (id) {
            case "discord": // From Discord
                if (!SurvivorBot.getInstance().isMaster()) {
                    String color = jsonData.get("color").getAsString();
                    user = jsonData.get("user").getAsString();
                    String userStatus = jsonData.get("userStatus").getAsString();
                    message = jsonData.get("message").getAsString();
                    String inviteURL = jsonData.get("inviteURL").getAsString();
                    String colorized = ChatColor.translateAlternateColorCodes('&', MessageListener.format(SurvivorBot.getChannelManager().getChannel(channel), ChatColor.getByChar(color) + user, message));

                    TextComponent root = new TextComponent(TextComponent.fromLegacyText(colorized));
                    root.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, inviteURL));
                    root.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.BOLD + "" + user + ChatColor.RESET + "\n" + (userStatus)).create()));

                    SurvivorBot.getChannelManager().getChannel(channel).announce(root);
                }
                break;
            case "announcement": // Announcement update
                if (!SurvivorBot.getInstance().isMaster()) {
                    message = jsonData.get("message").getAsString();
                    SurvivorBot.getInstance().setAnnouncement(message);
                    new Announcement(Bukkit.getOnlinePlayers().stream().collect(Collectors.toList()));
                }
                break;
            default: // From Other Server
                if (!id.equals(SurvivorBot.getChannelManager().getDefaultChannel().getName())) {
                    user = jsonData.get("user").getAsString();
                    message = jsonData.get("message").getAsString();
                    if (SurvivorBot.getChannelManager().getChannel(channel) != null) {
                        SurvivorBot.getChannelManager().getChannel(channel).announce(user + ": " + message);
                        if (SurvivorBot.getInstance().isMaster())
                            SurvivorBot.getInstance().getDJA().getTextChannelById(SurvivorBot.getChannelManager().getChannel(channel).getDiscordChannelLinkID()).sendMessage(user + ": " + message).queue();
                    }
                }
                break;
        }
    }

    public void onSubscribe(String ch, int subscribedChannels) {
        SurvivorBot.info("Jedis Listener Subscribed to :" + ch);
    }

}
