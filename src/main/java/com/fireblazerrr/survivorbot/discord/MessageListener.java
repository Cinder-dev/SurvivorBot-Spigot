package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class MessageListener implements IListener<MessageReceivedEvent> {

    public MessageListener(SurvivorBot plugin) {
        SurvivorBot plugin1 = plugin;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
//        // If the message contains a command
//        if (event.getMessage().toString().startsWith("/")) {
//            if (event.getMessage().getAuthor().getRolesForGuild(plugin.instance.client.getGuildByID(plugin.config.getString("plugin.discord.serverID"))).contains(plugin.instance.client.getRoleByID(plugin.config.getString("plugin.discord.staffID")))) {
//                JedisListener.publish(
//                        plugin.config.getString("plugin.general.redis.channel"),
//                        new Message(
//                                "discord",
//                                event.getMessage().getChannel().getName(),
//                                event.getMessage().getAuthor().getName(),
//                                event.getMessage().toString().replaceFirst("/", ""),
//                                "true"
//                        )
//                );
//            }
//            try {
//                event.getMessage().delete();
//            } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
//                e.printStackTrace();
//            }
//
//        }
//        // if not a command
//        else {
//            JedisListener.publish(
//                    plugin.config.getString("plugin.general.redis.channel"),
//                    new Message(
//                            "discord",
//                            event.getMessage().getChannel().getName(),
//                            event.getMessage().getAuthor().getName(),
//                            event.getMessage().toString(),
//                            "false"
//                    ));
//        }
    }
}
