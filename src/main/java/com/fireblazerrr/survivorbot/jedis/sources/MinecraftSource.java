package com.fireblazerrr.survivorbot.jedis.sources;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.entity.Player;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class MinecraftSource {

//    private SurvivorBot plugin;
//
//    public MinecraftSource(SurvivorBot plugin, Message message) {
////        this.plugin = plugin;
////
////        plugin.getServer().getOnlinePlayers().forEach(p -> BroadcastMessageToPlayer(p, message));
////
////        if (plugin.config.getBoolean("plugin.discord.master")) {
////            toDiscord(message);
////        }
//
//    }
//
//    private void toDiscord(Message message) {
//        IChannel channel = plugin.instance.client.getChannels().stream().filter(s -> s.getName().equals(message.channel)).findFirst().orElse(null);
//        if (channel != null) {
//            try {
//                channel.sendMessage("<" + message.user + "> " + message.message);
//            } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void BroadcastMessageToPlayer(Player player, Message message) {
////        String prefix = message.source.equals(plugin.config.getString("plugin.chat.settings.default")) ? "" : ChatColor.GREEN + plugin.config.getString("plugin.chat.channels." + message.source + ".prefix") + " " + ChatColor.RESET;
////        String sender = message.channel.equals(plugin.config.getString("plugin.chat.settings.default")) ? "<" + message.user + "> " : "<" + message.channel + ": " + message.user + "> ";
////        if (plugin.config.getList("plugin.players." + player.getUniqueId().toString() + ".memberOf").contains(message.channel))
////            plugin.getServer().getPlayer(player.getUniqueId()).sendMessage(prefix + sender + message.message);
//
//    }
}
