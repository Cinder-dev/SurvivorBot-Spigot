package com.fireblazerrr.survivorbot;

import com.fireblazerrr.survivorbot.spigot.CommandManager;
import com.fireblazerrr.survivorbot.utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivorBot extends JavaPlugin {

    private static final CommandManager commandManager = new CommandManager();

    private final Logger logger = new Logger(this.getClass());

    private static SurvivorBot plugin;

    public void onEnable(){
        logger.info("Enabling SurvivorBot!");
    }

    public void onDisable(){
        logger.info("Disabling SurvivorBot!");
    }


    // Getters and Setters

    public static SurvivorBot getPlugin() {
        return plugin;
    }
}

