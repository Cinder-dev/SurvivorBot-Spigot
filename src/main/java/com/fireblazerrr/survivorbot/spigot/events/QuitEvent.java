package com.fireblazerrr.survivorbot.spigot.events;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.jedis.JedisListener;
import com.fireblazerrr.survivorbot.jedis.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    private SurvivorBot plugin;

    public QuitEvent(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        JedisListener.publish(plugin.config.getString("plugin.chat.settings.default"),
                new Message(
                        plugin.config.getString("plugin.discord.serverID"),
                        plugin.config.getString("plugin.chat.settings.default"),
                        player.getName(),
                        "Left the game!"
                )
        );
    }

    public void unregister() {
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
