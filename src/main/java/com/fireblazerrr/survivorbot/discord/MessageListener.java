package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.utils.Announcement;
import com.fireblazerrr.survivorbot.utils.ColorUtils;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {

    private ColorUtils cu = new ColorUtils();

    public MessageListener() {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(SurvivorBot.getInstance().getAnnouncementChannelID())) {
            new Announcement(Bukkit.getOnlinePlayers().stream().collect(Collectors.toList()));
            return;
        }
        if (!event.getAuthor().isBot()) {
            String channelID = event.getMessage().getTextChannel().getId();
            String sender = getDiscordColor(event.getMember().getColor()) + event.getMessage().getAuthor().getName();
            String message = event.getMessage().getRawContent();

            SurvivorBot.getChannelManager().getChannels().stream()
                    .filter(channel -> channel.getDiscordChannelLinkID().equals(channelID))
                    .forEach(channel -> channel.getMembers()
                            .stream()
                            .map(Chatter::getPlayer)
                            .map(Player::spigot)
                            .forEach(spigot -> {
                                String colorized = ChatColor.translateAlternateColorCodes('&', format(channel, sender, message));
                                TextComponent root = new TextComponent(TextComponent.fromLegacyText(colorized));
                                root.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SurvivorBot.getInstance().getInviteURL()));
                                root.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.BOLD + "" + sender + ChatColor.RESET + "\n" + (SurvivorBot.getInstance().getDJA().getGuildById(event.getGuild().getId()).getMember(event.getMessage().getAuthor()).getGame().getName() == null ? "" : "Playing: " + SurvivorBot.getInstance().getDJA().getGuildById(event.getGuild().getId()).getMember(event.getMessage().getAuthor()).getGame().getName())).create()));
                                spigot.sendMessage(root);
                            }));

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "discord");
            jsonObject.addProperty("channel", channelID);
            jsonObject.addProperty("color", getDiscordColor(event.getMember().getColor()).getChar());
            jsonObject.addProperty("user", ChatColor.stripColor(sender));
            jsonObject.addProperty("userStatus", SurvivorBot.getInstance().getDJA().getGuildById(event.getGuild().getId()).getMember(event.getMessage().getAuthor()).getGame().getName() == null ? "" : "Playing: " + SurvivorBot.getInstance().getDJA().getGuildById(event.getGuild().getId()).getMember(event.getMessage().getAuthor()).getGame().getName());
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("inviteURL", SurvivorBot.getInstance().getInviteURL());
            SurvivorBot.getJedisPool().getResource().publish("survivor", jsonObject.toString());
        }
    }

    private ChatColor getDiscordColor(Color color) {
        return cu.getColorNameFromColor(color);
    }

    public static String format(Channel channel, String sender, String message) {
        String form = channel.applyFormat(SurvivorBot.getChannelManager().getDiscordFormat(), "")
                .replace("{prefix}", "")
                .replace("{suffix}", "")
                .replace("1$", "")
                .replace("2$", "");
        return String.format(form, sender, message);
    }
}
