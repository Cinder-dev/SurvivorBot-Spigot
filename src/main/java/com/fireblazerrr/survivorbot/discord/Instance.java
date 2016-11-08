package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.Bukkit;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class Instance {

    private SurvivorBot plugin;

    public volatile IDiscordClient client;

    private final AtomicBoolean reconnect = new AtomicBoolean(true);

    public Instance(SurvivorBot plugin) {
        this.plugin = plugin;
        if (this.plugin.config.getString("plugin.discord.token") != null) {
            login();

            client.getDispatcher().registerListener(new ReadyListener(this.plugin));
            client.getDispatcher().registerListener(new MessageListener(this.plugin));
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Token is required for bot use");
        }
    }

    private void login() {
        try {
            client = new ClientBuilder().withToken(plugin.config.getString("plugin.discord.token")).login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        reconnect.set(false);
        try {
            client.logout();
        } catch (DiscordException | RateLimitException e) {
            e.printStackTrace();
        }
    }
}
