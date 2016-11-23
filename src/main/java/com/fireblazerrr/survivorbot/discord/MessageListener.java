package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.ChatColor;
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
                .forEach(channel -> channel.announce(ChatColor.DARK_PURPLE + "<Discord:" + ChatColor.RESET + sender + ChatColor.DARK_PURPLE + ">: " + channel.getColor() + message));
    }
}
