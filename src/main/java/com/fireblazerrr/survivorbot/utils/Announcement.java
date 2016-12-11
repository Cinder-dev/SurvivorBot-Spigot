package com.fireblazerrr.survivorbot.utils;

import com.fireblazerrr.survivorbot.SurvivorBot;
import net.dv8tion.jda.core.entities.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Announcement {

    private String announcement = "";

    public Announcement(List<Player> players) {
        if (SurvivorBot.getInstance().getAnnouncementChannelID() != "" && SurvivorBot.getInstance().isUseAnnouncement()) {
            getLatestAnnouncement(messages -> {
                String message = messages.get(0).getContent();

                TextComponent component = new TextComponent();

                String[] words = message.split("\\s+");
                URL url = null;
                for (String word : words) {
                    try {
                        url = new URL(word);
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.toString()));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to open " + ChatColor.DARK_BLUE + url.toString()).create()));
                    } catch (MalformedURLException ignore) {

                    }
                }
                announcement = messages.get(0).getContent().replace(url != null ? url.toString() : "", url != null ? "&9" + url.toString() : "");
                Arrays.stream(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', SurvivorBot.getInstance().getAnnouncementHeader() + "\n" + announcement + "\n" + SurvivorBot.getInstance().getAnnouncementFooter()))).forEach(component::addExtra);

                players.stream().map(Player::spigot).forEach(spigot -> spigot.sendMessage(component));
            });
        }
    }

    public Announcement(Player player) {
        if (SurvivorBot.getInstance().getAnnouncementChannelID() != "" && SurvivorBot.getInstance().isUseAnnouncement()) {
            getLatestAnnouncement(messages -> {
                String message = messages.get(0).getContent();

                TextComponent component = new TextComponent();

                String[] words = message.split("\\s+");
                URL url = null;
                for (String word : words) {
                    try {
                        url = new URL(word);
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.toString()));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to open " + ChatColor.DARK_BLUE + url.toString()).create()));
                    } catch (MalformedURLException ignore) {

                    }
                }
                announcement = messages.get(0).getContent().replace(url != null ? url.toString() : "", url != null ? "&9" + url.toString() : "");
                Arrays.stream(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', SurvivorBot.getInstance().getAnnouncementHeader() + "\n" + announcement + "\n" + SurvivorBot.getInstance().getAnnouncementFooter()))).forEach(component::addExtra);

                player.spigot().sendMessage(component);
            });
        }
    }

    private void getLatestAnnouncement(Consumer<List<Message>> consumer) {
        SurvivorBot
                .getInstance()
                .getDJA()
                .getTextChannelById(SurvivorBot.getInstance().getAnnouncementChannelID())
                .getHistory()
                .retrievePast(1)
                .queue(consumer);
    }


}
