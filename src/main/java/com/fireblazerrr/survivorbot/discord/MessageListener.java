package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class MessageListener implements IListener<MessageReceivedEvent> {

    public MessageListener() {
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        String channelID = event.getMessage().getChannel().getID();
        String sender = event.getMessage().getAuthor().getName();
        String message = event.getMessage().toString();

        SurvivorBot.getChannelManager().getChannels().stream()
                .filter(channel -> channel.getDiscordChannelLinkID().equals(channelID))
                .forEach(channel -> {
                    channel.getMembers().stream()
                            .map(Chatter::getPlayer)
                            .map(Player::spigot)
                            .forEach(spigot -> {
                                String colorized = ChatColor.translateAlternateColorCodes('&', format(channel, sender, message));
                                TextComponent root = new TextComponent(TextComponent.fromLegacyText(colorized));
                                root.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SurvivorBot.getInstance().getInviteURL()));
                                root.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.BOLD + "" + ChatColor.AQUA + sender + ChatColor.RESET + "\n" + (event.getMessage().getAuthor().getStatus().getStatusMessage() == null ? "" : "Playing: " + event.getMessage().getAuthor().getStatus().getStatusMessage())).create()));
                                spigot.sendMessage(root);
                            });
                });
    }

    public String format(Channel channel, String sender, String message) {
        String form = channel.applyFormat(SurvivorBot.getChannelManager().getDiscordFormat(), "")
                .replace("{prefix}", "")
                .replace("{suffix}", "")
                .replace("1$", "")
                .replace("2$", "");
        return String.format(form, sender, message);
    }
}
