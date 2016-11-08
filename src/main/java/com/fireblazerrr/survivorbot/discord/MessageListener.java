package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.jedis.JedisListener;
import com.fireblazerrr.survivorbot.jedis.Message;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class MessageListener implements IListener<MessageReceivedEvent> {

    private SurvivorBot plugin;

    public MessageListener(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        // If the message contains a command
        if (event.getMessage().toString().startsWith("/")) {
            if (event.getMessage().getAuthor().getRolesForGuild(plugin.instance.client.getGuildByID(plugin.config.getString("plugin.discord.serverID"))).contains(plugin.instance.client.getRoleByID(plugin.config.getString("plugin.discord.staffID")))) {
                JedisListener.publish(
                        event.getMessage().getChannel().getName(),
                        new Message(
                                "discord",
                                event.getMessage().getChannel().getName(),
                                "`command",
                                event.getMessage().toString().replaceFirst("/", "")
                        )
                );
            }
            try {
                event.getMessage().delete();
            } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                e.printStackTrace();
            }

        }
        // if not a command
        else {
            JedisListener.publish(
                    event.getMessage().getChannel().getName(),
                    new Message(
                            "discord",
                            event.getMessage().getChannel().getName(),
                            event.getMessage().getAuthor().getName(),
                            event.getMessage().toString()));
        }
    }
}
