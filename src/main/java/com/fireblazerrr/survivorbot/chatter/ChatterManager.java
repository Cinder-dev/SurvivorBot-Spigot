package com.fireblazerrr.survivorbot.chatter;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChatterManager {
    private Map<String, Chatter> chatters = new HashMap<>();
    private ChatterStorage storage;

    public ChatterManager() {

    }

    public Chatter addChatter(Player player) {
        String name = player.getName().toLowerCase();
        Chatter chatter = this.chatters.get(name);
        if (chatter != null) {
            return chatter;
        } else {
            Object chatter1 = this.storage.load(player.getUniqueId());
            if (chatter1 == null) {
                SurvivorBot.severe("Null chatter for: " + player.getName() + " was detected, wiping all player info and attempting to load bogus chatter.");
                chatter1 = new StandardChatter(this.storage, player);
                this.chatters.put(name, (Chatter) chatter1);
                this.storage.flagUpdate((Chatter) chatter1);
            } else {
                this.chatters.put(name, (Chatter) chatter1);
            }

            return (Chatter) chatter1;
        }
    }

    public void clear() {
        this.chatters.clear();
        this.storage = null;
    }

    public boolean hasChatter(Player player) {
        return this.chatters.containsKey(player.getName().toLowerCase());
    }

    public Chatter getChatter(Player player) {
        return this.chatters.get(player.getName().toLowerCase());
    }

    public Chatter getChatter(String name) {
        return this.chatters.get(name.toLowerCase());
    }

    public Collection<Chatter> getChatters() {
        return this.chatters.values();
    }

    public ChatterStorage getStorage() {
        return this.storage;
    }

    public void setStorage(ChatterStorage storage) {
        this.storage = storage;
    }

    public void removeChatter(Chatter chatter) {
        this.storage.removeChatter(chatter);
        chatter.disconnect();
        String name = chatter.getPlayer().getName().toLowerCase();
        this.chatters.remove(name);
    }

    public void removeChatter(Player player) {
        this.removeChatter(this.getChatter(player));
    }

    public void reset() {
        this.chatters.clear();
    }
}
