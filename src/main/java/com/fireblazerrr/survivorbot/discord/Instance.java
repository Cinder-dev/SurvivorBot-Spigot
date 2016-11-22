package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.concurrent.atomic.AtomicBoolean;

public class Instance {

    private boolean master = true;
    private String token = "";
    private String adminRankID = "";
    private String serverID = "";
    private volatile IDiscordClient client;

    private static final ReadyListener readyListener = new ReadyListener();
    private static final MessageListener messageListener = new MessageListener();

    private final AtomicBoolean reconnect = new AtomicBoolean(true);

    public Instance() {
    }

    public void setupInstance() {
        if (!token.equals("")) {
            login();

            client.getDispatcher().registerListener(readyListener);
            client.getDispatcher().registerListener(messageListener);
        } else {
            SurvivorBot.severe("Token is required for bot use");
        }
    }

    private void login() {
        try {
            client = new ClientBuilder().withToken(token).login();
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

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdminRankID() {
        return adminRankID;
    }

    public void setAdminRankID(String adminRankID) {
        this.adminRankID = adminRankID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public IDiscordClient getClient() {
        return client;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }
}
