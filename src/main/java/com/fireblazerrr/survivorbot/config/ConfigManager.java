package com.fireblazerrr.survivorbot.config;

import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ConfigManager {

    public FileConfiguration pluginConfig;
    public FileConfiguration discordConfig;

    public ConfigManager(Plugin plugin) {
        pluginConfig = loadResource(plugin, "pluginConfig.yml");
        discordConfig = loadResource(plugin, "discordConfig.yml");
    }

    /**
     * Load pre-made config file from resources
     */
    public static FileConfiguration loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(resourceFile);
    }
}
