package com.fireblazerrr.survivorbot.chatter;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YMLChatterStorage implements ChatterStorage {

    private final File chatterFolder;
    private Set<Chatter> updates = new HashSet<>();

    public YMLChatterStorage(File chatterFolder) {
        this.chatterFolder = chatterFolder;
    }

    @Override
    public void flagUpdate(Chatter chatter) {
        this.updates.add(chatter);
    }

    @Override
    public Chatter load(String name) {
        File folder = new File(this.chatterFolder, name.substring(0, 1).toLowerCase());
        File file = new File(folder, name + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        try {
            folder.mkdirs();
            if (file.exists()) {
                config.load(file);
            }
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        Player player = Bukkit.getServer().getPlayerExact(name);
        if (player == null) {
            return null;
        } else {
            StandardChatter chatter = new StandardChatter(this, player);
            this.loadChannels(chatter, config);
            this.loadActiveChannel(chatter, config);
            this.loadIgnores(chatter, config);
            this.loadMuted(chatter, config);
            return chatter;
        }
    }

    @Override
    public void removeChatter(Chatter chatter) {
        this.update(chatter);
        this.updates.remove(chatter);
    }

    @Override
    public void update() {
        if (!this.updates.isEmpty()) {
            SurvivorBot.info("Saving players");

            this.updates.forEach(this::update);

            SurvivorBot.info("Save complete");
        }
    }

    @Override
    public void update(Chatter chatter) {
        YamlConfiguration config = new YamlConfiguration();
        String name = chatter.getName();
        config.set("name", name);
        if (chatter.getActiveChannel() != null) {
            if (chatter.getActiveChannel().isTransient()) {
                config.set("activeChannel", chatter.getLastActiveChannel().getName());
            } else {
                config.set("activeChannel", chatter.getActiveChannel().getName());
            }
        }

        ArrayList<String> channels = new ArrayList<>();

        chatter.getChannels().stream().filter(channel -> !channel.isTransient()).map(Channel::getName).forEach(channels::add);

        config.set("channels", channels);
        config.set("ignores", new ArrayList<>(chatter.getIgnores()));
        config.set("muted", chatter.isMuted());
        config.set("autojoin", Boolean.FALSE);
        File folder1 = new File(this.chatterFolder, name.substring(0, 1).toLowerCase());
        File file1 = new File(folder1, name + ".yml");

        try {
            config.save(file1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadActiveChannel(Chatter chatter, MemoryConfiguration config) {
        ChannelManager channelManager = SurvivorBot.getChannelManager();
        Channel defaultChannel = channelManager.getDefaultChannel();
        Channel activeChannel = channelManager.getChannel(config.getString("activeChannel", ""));
        if (activeChannel == null || !chatter.hasChannel(activeChannel)) {
            activeChannel = defaultChannel;
        }

        chatter.setActiveChannel(defaultChannel, false, false);
        chatter.setActiveChannel(activeChannel, false, false);
    }

    private void loadChannels(Chatter chatter, MemoryConfiguration config) {
        ChannelManager channelManager = SurvivorBot.getChannelManager();
        Set<Channel> channels = new HashSet<>();
        config.addDefault("channels", new ArrayList<>());
        List<String> channelNames = config.getStringList("channels");

        channelNames.stream().map(channelManager::getChannel).filter(channel -> channel != null && chatter.canJoin(channel, channel.getPassword()) == Chatter.Result.ALLOWED).forEach(channels::add);

        boolean autojoin1 = config.getBoolean("autojoin", true);

        Iterator i = channelManager.getChannels().iterator();


        Channel channel;
        while (true) {
            do {
                if (!i.hasNext()) {
                    i = channels.iterator();

                    while (i.hasNext()) {
                        if (chatter.shouldForceLeave((Channel) i.next())) {
                            i.remove();
                        }
                    }

                    if (channels.isEmpty()) {
                        channels.add(channelManager.getDefaultChannel());
                    }

                    i = channels.iterator();

                    while (i.hasNext()) {
                        channel = (Channel) i.next();
                        channel.addMember(chatter, false, false);
                    }

                    return;
                }

                channel = (Channel) i.next();
            } while ((!autojoin1 || !chatter.shouldAutoJoin(channel)) && !chatter.shouldForceJoin(channel));

            channels.add(channel);
        }
    }

    private void loadIgnores(Chatter chatter, MemoryConfiguration config) {
        config.addDefault("ignores", new ArrayList<>());
        List<String> ignores = config.getStringList("ignores");

        ignores.forEach(s -> chatter.setIgnore(s, true, false));
    }

    private void loadMuted(Chatter chatter, MemoryConfiguration config) {
        boolean muted = config.getBoolean("muted", false);
        chatter.setMuted(muted, false);
    }
}


















