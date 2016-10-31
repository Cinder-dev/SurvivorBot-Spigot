package com.fireblazerrr.survivorbot.spigot;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.jedis.JedisListener;
import com.fireblazerrr.survivorbot.jedis.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getPlayer().getName(),
                        event.getMessage()
                )
        );
    }

    @EventHandler
    public void playerAchievement(PlayerAchievementAwardedEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getPlayer().getName(),
                        "Earned an achievement!"
                )
        );
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getPlayer().getName(),
                        "Left the game!"
                )
        );
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getDeathMessage(),
                        ""
                )
        );
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getPlayer().getName(),
                        "Joined the game!"
                )
        );
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        JedisListener.publish(SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                new Message(
                        SurvivorBot.configManager.pluginConfig.getString("serverID"),
                        SurvivorBot.configManager.pluginConfig.getString("primary channel"),
                        event.getPlayer().getName(),
                        "Left the game!"
                )
        );
    }
}
