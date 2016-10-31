package com.fireblazerrr.survivorbot;

import com.fireblazerrr.survivorbot.config.ConfigManager;
import com.fireblazerrr.survivorbot.discord.Instance;
import com.fireblazerrr.survivorbot.jedis.JedisManager;
import com.fireblazerrr.survivorbot.spigot.CommandListener;
import com.fireblazerrr.survivorbot.spigot.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivorBot extends JavaPlugin {

    private JedisManager jedisManager;
    private Thread jedisManagerThread;

    public static ConfigManager configManager;

    public static Instance instance;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);

        // Create Discord Bot Instance
        if (configManager.discordConfig.getBoolean("master")) {
            instance = new Instance();
        }

        // Create Jedis Event Listener
        jedisManager = new JedisManager();
        jedisManagerThread = new Thread(jedisManager);
        jedisManagerThread.start();

        // Create Spigot Event Listener
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Create Command Listeners
        CommandListener cl = new CommandListener();

        this.getCommand("channels").setExecutor(cl);
        this.getCommand("online").setExecutor(cl);
        this.getCommand("ranks").setExecutor(cl);
        this.getCommand("s").setExecutor(cl);
    }

    @Override
    public void onDisable() {
        jedisManager.destroy();
        if (configManager.discordConfig.getBoolean("master"))
            instance.terminate();

        jedisManagerThread.stop();
    }
}
