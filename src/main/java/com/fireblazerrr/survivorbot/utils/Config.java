package com.fireblazerrr.survivorbot.utils;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.configuration.Configuration;

public class Config {

    private SurvivorBot plugin;
    private Configuration config;

    public Config(SurvivorBot plugin) {
        this.plugin = plugin;

        config = this.plugin.getConfig().getRoot();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }
}
