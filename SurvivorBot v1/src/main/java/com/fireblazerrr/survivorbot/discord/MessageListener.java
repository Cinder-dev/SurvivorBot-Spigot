package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.utils.Announcement;
import com.fireblazerrr.survivorbot.utils.ColorUtils;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {

    private ColorUtils cu = new ColorUtils();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(SurvivorBot.getInstance().getAnnouncementChannelID())) {
            new Announcement(new ArrayList<>(Bukkit.getOnlinePlayers()));
            return;
        }
        if (!event.getAuthor().isBot()) {
            String channelID = event.getMessage().getTextChannel().getId();
            String sender = getDiscordColor(event.getMember().getColor()) + event.getMessage().getAuthor().getName();
            Member member = event.getMember();
            String game = member.getGame() == null ? "" : member.getGame().getName();
            String message = event.getMessage().getRawContent();
            Channel channel = SurvivorBot.getChannelManager().getChannel(channelID);

            String colorizedMessage = ChatColor.translateAlternateColorCodes('&', format(channel, sender, message));
            TextComponent root = new TextComponent(TextComponent.fromLegacyText(colorizedMessage));
            root.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SurvivorBot.getInstance().getInviteURL()));
            root.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.BOLD + "" + sender + ChatColor.RESET + "\n" + game).create()));

            channel.announce(root);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "discord");
            jsonObject.addProperty("channel", channelID);
            jsonObject.addProperty("color", getDiscordColor(event.getMember().getColor()).getChar());
            jsonObject.addProperty("user", ChatColor.stripColor(sender));
            jsonObject.addProperty("userStatus", game);
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("inviteURL", SurvivorBot.getInstance().getInviteURL());
            SurvivorBot.publish("survivor", jsonObject.toString());
        }
    }

    private ChatColor getDiscordColor(Color color) {
        return ColorUtils.getColorNameFromColor(color);
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
