package com.fireblazerrr.survivorbot.utils;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    static SurvivorBot plugin;

    public Log(SurvivorBot plugin) {
        Log.plugin = plugin;
    }

    private static final Logger logg = Logger.getLogger("Minecraft");

    public static void info(String message) {
        logg.log(Level.INFO, String.format("%s %s", "[SurvivorBot]", message));
    }

    public static void error(String message) {
        logg.log(Level.SEVERE, String.format("%s %s", "[SurvivorBot]", message));
        plugin.onDisable();
    }

    public static void debug(String message) {
        if (plugin.config.getBoolean("plugin.general.debug"))
            logg.log(Level.INFO, String.format("%s%s %s", "[SurvivorBot]", "[Debug]", message));
    }

    public static void warning(String message) {
        logg.log(Level.WARNING, String.format("%s %s", "[SurvivorBot]", message));
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }
}
