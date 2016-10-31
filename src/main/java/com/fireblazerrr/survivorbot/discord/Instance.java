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

    public volatile IDiscordClient client;

    private final AtomicBoolean reconnect = new AtomicBoolean(true);

    public Instance() {
        if (SurvivorBot.configManager.discordConfig.getString("token") != null) {
            login();

            client.getDispatcher().registerListener(new ReadyListener());
            client.getDispatcher().registerListener(new MessageListener());
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Token is required for bot use");
        }
    }

    private void login() {
        try {
            client = new ClientBuilder().withToken(SurvivorBot.configManager.discordConfig.getString("token")).login();
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
