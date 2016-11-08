package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class JedisListener extends JedisPubSub {

    private SurvivorBot plugin;

    public JedisListener(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessage(String channel, String message) {
        Message received = new Message(message);

        // Broadcast messages received from discord
        if (received.source.equals("discord")) {
            // If it is from the servers channel
            if (received.channel.equals(plugin.config.getString("plugin.chat.settings.default"))) {
                // If the message contains a command
                if (received.user.equals("`command")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), received.message);
                } else {
                    broadcastToUsers(ChatColor.RED + "[DISCORD] " + ChatColor.WHITE + "<" + received.user + "> " + received.message);
                }
            }
            // If the message is from the staff channel
            else if (received.channel.equals(plugin.config.getString("plugin.chat.settings.staff"))) {
                // Not a command
                if (!received.user.equals("`command")) {
                    broadcastToUsers(ChatColor.RED + "[DISCORD:" + received.channel + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message, true);
                }
            }
            // If it is from a watched channel
            else {
                // Not a command
                if (!received.user.equals("`command")) {
                    broadcastToUsers(ChatColor.RED + "[DISCORD:" + received.channel + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message);
                }
            }
        }
        // Broadcast message from other servers
        else if (!received.source.equals(plugin.config.getString("plugin.discord.serverID"))) {
            // If message is from staff
            if (received.channel.equals(plugin.config.getString("plugin.chat.settings.staff"))) {
                // Not a command
                if (!received.user.equals("`command")) {
                    broadcastToUsers(ChatColor.DARK_GREEN + "[" + received.channel + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message, true);
                }
            }
            // From other server regular channel
            else {
                broadcastToUsers(ChatColor.DARK_GREEN + "[" + received.source + "] " + ChatColor.WHITE + "<" + received.user + "> " + received.message);
            }
        }

        // Send messages from all servers to their discord channel
        if (plugin.config.getBoolean("master") && !received.source.equals("discord")) {
            plugin.instance.client.getChannels().stream().filter(ch -> ch.getName().equals(received.channel)).forEach(ch -> {
                try {
                    ch.sendMessage("<" + received.user + "> " + received.message, false);
                } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPMessage(String pattern, String channel,
                           String message) {
    }

    private void broadcastToUsers(String message) {
        broadcastToUsers(message, false);
    }

    /**
     * Send message to users or Ops/staff
     */
    private void broadcastToUsers(String message, boolean opOnly) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (opOnly) {
                if (player.isOp() || player.hasPermission("survivorbot.staff")) {
                    player.sendMessage(message);
                }
            } else {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Send message to Redis
     */
    public static void publish(String channel, Message message) {
        Jedis jedis = new Jedis("localhost");
        jedis.publish(channel, message.toString());
        jedis.close();
    }
}
