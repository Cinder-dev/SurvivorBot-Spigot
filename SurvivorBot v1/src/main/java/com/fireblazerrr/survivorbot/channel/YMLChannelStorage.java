package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YMLChannelStorage implements ChannelStorage {
    private final File channelFolder;
    private Map<Channel, FileConfiguration> configs = new HashMap<>();
    private Set<Channel> updates = new HashSet<>();

    public YMLChannelStorage(File channelFolder) {
        this.channelFolder = channelFolder;
    }

    @Override
    public void addChannel(Channel channel) {
        if (!this.configs.containsKey(channel) && !channel.isTransient()) {
            File file = new File(this.channelFolder, channel.getName() + ".yml");
            YamlConfiguration config = new YamlConfiguration();

            try {
                if (file.exists()) {
                    config.load(file);
                }
            } catch (IOException | InvalidConfigurationException ex) {
                ex.printStackTrace();
            }

            this.configs.put(channel, config);
            this.flagUpdate(channel);
        }
    }

    @Override
    public void flagUpdate(Channel channel) {
        if (!channel.isTransient()) {
            this.updates.add(channel);
        }
    }

    @Override
    public Channel load(String name) {
        File file = new File(this.channelFolder, name + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (InvalidConfigurationException e) {
            SurvivorBot.severe("Could not open file " + file.getName());
            e.printStackTrace();
        } catch (IOException e) {
            SurvivorBot.severe("Could not load channel " + file.getName());
            e.printStackTrace();
        }

        String nick = config.getString("nick", name);
        String format = config.getString("format", "{default}");
        String password = config.getString("password", "");
        String discordChannelLinkID = config.getString("discordChannelLinkID", "");
        ChatColor color = Messaging.parseColor(config.getString("color", "WHITE"));
        if (color == null) {
            SurvivorBot.warning("The color \'" + config.getString("color") + "\' is not valid.");
            color = ChatColor.WHITE;
        }

        int distance = config.getInt("distance", 0);
        boolean shortcut;
        if (config.contains("shortcutAllowed")) {
            shortcut = config.getBoolean("shortcutAllowed", true);
        } else {
            shortcut = config.getBoolean("shortcut", true);
        }

        boolean verbose = config.getBoolean("verbose", true);
        boolean crossWorld = config.getBoolean("crossworld", true);
        boolean muted = config.getBoolean("muted", false);
        config.addDefault("worlds", new ArrayList());
        config.addDefault("bans", new ArrayList());
        config.addDefault("mutes", new ArrayList());
        config.addDefault("moderators", new ArrayList());
        HashSet worlds = new HashSet(config.getStringList("worlds"));
        HashSet bans = new HashSet(config.getStringList("bans"));
        HashSet mutes = new HashSet(config.getStringList("mutes"));
        HashSet moderators = new HashSet(config.getStringList("moderators"));
        StandardChannel channel = new StandardChannel(this, name, nick, SurvivorBot.getChannelManager());
        channel.setFormat(format);
        channel.setPassword(password);
        channel.setDiscordChannelLinkID(discordChannelLinkID);
        channel.setColor(color);
        channel.setDistance(distance);
        channel.setShortcutAllowed(shortcut);
        channel.setVerbose(verbose);
        channel.setMuted(muted);
        channel.setCrossWorld(crossWorld);
        channel.setWorlds(worlds);
        channel.setBans(bans);
        channel.setMutes(mutes);
        channel.setModerators(moderators);
        this.addChannel(channel);
        return channel;
    }

    @Override
    public Set<Channel> loadChannels() {
        HashSet channels = new HashSet();
        String[] list = this.channelFolder.list();

        for (String aList : list) {
            String name = aList;
            name = name.substring(0, name.lastIndexOf(46));
            Channel channel = this.load(name);
            this.addChannel(channel);
            channels.add(channel);
        }

        return channels;
    }

    @Override
    public void removeChannel(Channel channel) {
        this.configs.remove(channel);
        this.flagUpdate(channel);
    }

    @Override
    public void update() {
        SurvivorBot.info("Saving channels");
        updates.forEach(this::update);
        SurvivorBot.info("Save complete");
    }

    @Override
    public void update(Channel c) {
        Channel channel = SurvivorBot.getChannelManager().getChannel(c.getName());
        File file = new File(this.channelFolder, c.getName() + ".yml");
        if (channel != null) {
            FileConfiguration config = this.configs.get(channel);
            config.options().copyDefaults(true);
            config.set("name", channel.getName());
            config.set("nick", channel.getNick());
            config.set("format", channel.getFormat());
            config.set("password", channel.getPassword());
            config.set("discordChannelLinkID", channel.getDiscordChannelLinkID());
            config.set("color", channel.getColor().name());
            config.set("distance", channel.getDistance());
            config.set("shortcut", channel.isShortcutAllowed());
            config.set("verbose", channel.isVerbose());
            config.set("crossworld", channel.isCrossWorld());
            config.set("muted", channel.isMuted());
            config.set("worlds", new ArrayList(channel.getWorlds()));
            config.set("bans", new ArrayList(channel.getBans()));
            config.set("mutes", new ArrayList(channel.getMutes()));
            config.set("moderators", new ArrayList(channel.getModerators()));

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
        }
    }
}
