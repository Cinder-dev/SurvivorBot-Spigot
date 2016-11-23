package com.fireblazerrr.survivorbot.spigot.listeners;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final SurvivorBot plugin;

    public PlayerListener(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String msg = event.getMessage();
        final String format = event.getFormat();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> SurvivorBot.getMessageHandler().handle(player, msg, format));
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreproccess(PlayerCommandPreprocessEvent event) {
        String input = event.getMessage().substring(1);
        String[] args = input.split(" ");
        Channel channel = SurvivorBot.getChannelManager().getChannel(args[0]);
        if (channel != null && channel.isShortcutAllowed()) {
            event.setCancelled(true);
            SurvivorBot.getCommandHandler().dispatch(event.getPlayer(), "ch qm", args);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        SurvivorBot.getChatterManager().addChatter(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        SurvivorBot.getChatterManager().removeChatter(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        SurvivorBot.getChatterManager().getChatter(event.getPlayer()).refocus();
    }
}
