package com.fireblazerrr.survivorbot.spigot.events;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private SurvivorBot plugin;

    public JoinEvent(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

//        JedisListener.publish(plugin.config.getString("plugin.chat.settings.default"),
//        new Message(
//                plugin.config.getString("plugin.discord.serverID"),
//                plugin.config.getString("plugin.chat.settings.default"),
//                        player.getName(),
//                        "Joined the game!"
//                )
//        );
    }

    public void unregister() {
        PlayerJoinEvent.getHandlerList().unregister(this);
    }
}
