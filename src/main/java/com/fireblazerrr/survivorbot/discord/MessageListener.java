package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
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
                .forEach(channel -> channel.announce(format(channel, sender, message)));
    }

    public String format(Channel channel, String sender, String message)
    {
        String form = channel.applyFormat(channel.getFormat(), "");
        form = form.replace("{prefix}", "");
        form = form.replace("{suffix}", "");
        form = form.replace("1$", "");
        form = form.replace("2$", "");
        return String.format(form, SurvivorBot.getChannelManager().getDiscordFormat().replace("{sender}", sender), message);
    }
}
