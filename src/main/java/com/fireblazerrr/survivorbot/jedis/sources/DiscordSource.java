package com.fireblazerrr.survivorbot.jedis.sources;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.entity.Player;

//public class DiscordSource {

//    public DiscordSource(SurvivorBot plugin, Message message) {
//        SurvivorBot plugin1 = plugin;
//
////        if (message.channel.equals(plugin.instance.client.getChannelByID(plugin.config.getString("plugin.discord.primaryChannelID")).getName()) && message.command.equals("true")) {
////            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), message.message);
////        } else {
////            plugin.getServer().getOnlinePlayers().forEach(p -> BroadcastMessageToPlayer(p, message));
////        }
//
//
////        // If it is from the servers channel
////        if (message.channel.equals(plugin.config.getString("plugin.chat.settings.default"))) {
////            // If the message contains a command
////            if (received.user.equals("`command")) {
////                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), received.message);
////            } else {
////                broadcastToUsers(ChatColor.RED + "[DISCORD] " + ChatColor.WHITE + "<" + received.user + "> " + received.message);
////            }
////        }
////        // If the message is from the staff channel
////        else if (received.channel.equals(plugin.config.getString("plugin.chat.settings.staff"))) {
////            // Not a command
////            if (!received.user.equals("`command")) {
////                broadcastToUsers(ChatColor.RED + "[DISCORD:" + received.channel + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message, true);
////            }
////        }
////        // If it is from a watched channel
////        else {
////            // Not a command
////            if (!received.user.equals("`command")) {
////                broadcastToUsers(ChatColor.RED + "[DISCORD:" + received.channel + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message);
////            }
////        }
//    }
//
//    private void BroadcastMessageToPlayer(Player player, Message message) {
////        String prefix = ChatColor.LIGHT_PURPLE + plugin.config.getString("plugin.chat.settings.discordPrefix") + " " + ChatColor.RESET;
////        String sender;
////        if (message.channel.equals(plugin.config.getString("plugin.chat.settings.default"))) {
////            sender = "<" + message.user + "> ";
////        } else {
////            sender = "<" + message.channel + ": " + message.user + "> ";
////        }
////        if (plugin.config.getList("plugin.players." + player.getUniqueId().toString() + ".memberOf").contains(message.channel))
////            plugin.getServer().getPlayer(player.getUniqueId()).sendMessage(prefix + sender + message.message);
//
//    }
//}
