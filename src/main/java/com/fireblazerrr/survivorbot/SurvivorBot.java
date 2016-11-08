package com.fireblazerrr.survivorbot;

import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.discord.Instance;
import com.fireblazerrr.survivorbot.jedis.JedisManager;
import com.fireblazerrr.survivorbot.spigot.commands.ChannelsCommand;
import com.fireblazerrr.survivorbot.spigot.events.*;
import com.fireblazerrr.survivorbot.utils.Config;
import com.fireblazerrr.survivorbot.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SurvivorBot extends JavaPlugin {

    public static String name;
    public static String version;
    public static final String prefix = ChatColor.WHITE + "["
            + ChatColor.DARK_GREEN + "SurvivorBot" + ChatColor.WHITE + "] "
            + ChatColor.RESET;

    public static SurvivorBot plugin;
    public Config config;
    public static boolean enabled = true;

    public static Log logger;

    // Spigot Listeners
    public static ChatEvent chatEventListener;
    public static AchievementEvent achievementEventListener;
    public static KickEvent kickEventListener;
    public static DeathEvent deathEventListener;
    public static JoinEvent joinEventListener;
    public static QuitEvent quitEventListener;

    private JedisManager jedisManager;
    private Thread jedisManagerThread;


    public ChannelManager channelManager;

    public Instance instance;

    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        version = this.getDescription().getVersion();

        getServer().getConsoleSender().sendMessage(ChatColor.RESET + "///// Enabling SurvivorBot /////");

        plugin = this;
        getServer().getConsoleSender().sendMessage(prefix + "Setting instance [ " + ChatColor.GREEN + "OK" + ChatColor.RESET + " ]");

        config = new Config(plugin);
        getServer().getConsoleSender().sendMessage(prefix + "Loading configuration [ " + ChatColor.GREEN + "OK" + ChatColor.RESET + " ]");

        channelManager = new ChannelManager(plugin);

        // Create Discord Bot Instance
        if (config.getBoolean("plugin.discord.master")) {
            instance = new Instance(plugin);
        }

        // Create Jedis Event Listener
        jedisManager = new JedisManager(plugin);
        jedisManagerThread = new Thread(jedisManager);
        jedisManagerThread.start();

        registerEvents();
        registerCommands();

        enabled = true;
    }


    public void onReload() {
        onDisable();
        onEnable();
    }

    /**
     * Register all used event listeners
     */
    private void registerEvents() {
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.RESET + "Registering listeners");
        chatEventListener = new ChatEvent(plugin);
        achievementEventListener = new AchievementEvent(plugin);
        kickEventListener = new KickEvent(plugin);
        deathEventListener = new DeathEvent(plugin);
        joinEventListener = new JoinEvent(plugin);
        quitEventListener = new QuitEvent(plugin);

        getServer().getPluginManager().registerEvents(chatEventListener, plugin);
        getServer().getPluginManager().registerEvents(achievementEventListener, plugin);
        getServer().getPluginManager().registerEvents(kickEventListener, plugin);
        getServer().getPluginManager().registerEvents(deathEventListener, plugin);
        getServer().getPluginManager().registerEvents(joinEventListener, plugin);
        getServer().getPluginManager().registerEvents(quitEventListener, plugin);
    }

    /**
     * Unregister all used event listeners
     */
    private void unregisterEvents() {
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.RESET + "Unregistering listeners");
        chatEventListener.unregister();
        achievementEventListener.unregister();
        kickEventListener.unregister();
        deathEventListener.unregister();
        joinEventListener.unregister();
        quitEventListener.unregister();
    }

    /**
     * Register all command executors
     */
    private void registerCommands() {
        Map<String, CommandExecutor> commands = new HashMap<>();
        commands.put("ch", new ChannelsCommand(plugin));
        commands.forEach((s, commandExecutor) -> Bukkit.getPluginCommand(s).setExecutor(commandExecutor));
    }

    public void onDisable() {
        if (!enabled)
            return;

        getServer().getConsoleSender().sendMessage(ChatColor.RESET + "///// SurvivorBot is being disabled /////");

        unregisterEvents();

        getServer().getConsoleSender().sendMessage(ChatColor.RESET + "/////////////////////////////////////////");

        enabled = false;


        jedisManager.destroy();
        if (config.getBoolean("plugin.discord.master"))
            instance.terminate();

        jedisManagerThread.stop();
    }
}
