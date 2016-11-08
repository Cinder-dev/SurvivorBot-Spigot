package com.fireblazerrr.survivorbot.spigot.events;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.jedis.JedisListener;
import com.fireblazerrr.survivorbot.jedis.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    private SurvivorBot plugin;

    public DeathEvent(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        JedisListener.publish(plugin.config.getString("plugin.chat.settings.default"),
                new Message(
                        plugin.config.getString("plugin.discord.serverID"),
                        plugin.config.getString("plugin.chat.settings.default"),
                        event.getDeathMessage(),
                        ""
                )
        );
    }

    public void unregister() {
        PlayerDeathEvent.getHandlerList().unregister(this);
    }
}
