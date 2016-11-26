package com.fireblazerrr.survivorbot.utils;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.channel.StandardChannel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ConfigManager {

    public ConfigManager() {

    }

    public void load(File file) {
        YamlConfiguration config = new YamlConfiguration();

        try {
            if (file.exists()) {
                config.load(file);
            }
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

        config.setDefaults(this.getDefaults());
        config.options().header(
                "============================================================================           \n" +
                        "Color Codes (Used with &<value>)  | Formatting Codes (Used with &<value>)      \n" +
                        "0 : Black                         | k : Obfuscated 'Magic'                     \n" +
                        "1 : Dark Blue                     | l : Bold                                   \n" +
                        "2 : Dark Green                    | m : Strikethrough                          \n" +
                        "3 : Dark Aqua                     | n : Underline                              \n" +
                        "4 : Dark Red                      | o : Italic                                 \n" +
                        "5 : Dark Purple                   | r : Reset                                  \n" +
                        "6 : Gold                          |                                            \n" +
                        "5 : Gray                          |                                            \n" +
                        "8 : Dark Gray                     |                                            \n" +
                        "9 : Blue                          |                                            \n" +
                        "a : Green                         |                                            \n" +
                        "b : Aqua                          |                                            \n" +
                        "c : Red                           |                                            \n" +
                        "d : Light Purple                  |                                            \n" +
                        "e : Yellow                        |                                            \n" +
                        "f : White                         |                                            \n" +
                        "============================================================================   \n" +
                        "Formatting Tags                                                                \n" +
                        "{name} : the channel's name                                                    \n" +
                        "{nick} : the channel's nick                                                    \n" +
                        "{color} : the channel's color                                                  \n" +
                        "{msg} : the message                                                            \n" +
                        "{sender} : the sender's display name                                           \n" +
                        "{plainsender} : senders user name                                              \n" +
                        "{world} : senders world                                                        \n" +
                        "{prefix} : senders scoreboard team prefix, formatting and color for sender     \n" +
                        "{suffix} : senders scoreboard team suffix                                      \n" +
                        "{group} : senders scoreboard team name - unimplemented                         \n" +
                        "{groupprefix} : - unimplemented                                                \n" +
                        "{groupsuffix} : - unimplemented                                                \n" +
                        "{convoaddress} : To or From (only used for private messages)                   \n" +
                        "{convopartner} : the sender or receiver (only used for private messages)       \n" +
                        "============================================================================   \n");
        ChannelManager channelManager = SurvivorBot.getChannelManager();
        if (config.getBoolean("moderator-permissions.can-kick")) {
            channelManager.addModPermission(Chatter.Permission.KICK);
        }

        if (config.getBoolean("moderator-permissions.can-ban")) {
            channelManager.addModPermission(Chatter.Permission.BAN);
        }

        if (config.getBoolean("moderator-permissions.can-mute")) {
            channelManager.addModPermission(Chatter.Permission.MUTE);
        }

        if (config.getBoolean("moderator-permissions.can-remove-channel")) {
            channelManager.addModPermission(Chatter.Permission.REMOVE);
        }

        if (config.getBoolean("moderator-permissions.can-modify-nick")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_NICK);
        }

        if (config.getBoolean("moderator-permissions.can-modify-color")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_COLOR);
        }

        if (config.getBoolean("moderator-permissions.can-modify-distance")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_DISTANCE);
        }

        if (config.getBoolean("moderator-permissions.can-modify-password")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_PASSWORD);
        }

        if (config.getBoolean("moderator-permissions.can-modify-format")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_FORMAT);
        }

        if (config.getBoolean("moderator-permissions.can-modify-shortcut")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_SHORTCUT);
        }

        if (config.getBoolean("moderator-permissions.can-modify-verbose")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_VERBOSE);
        }

        if (config.getBoolean("moderator-permissions.can-modify-focusable")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_FOCUSABLE);
        }

        if (config.getBoolean("moderator-permissions.can-modify-crossworld")) {
            channelManager.addModPermission(Chatter.Permission.MODIFY_CROSSWORLD);
        }

        if (config.getBoolean("moderator-permissions.can-color-messages")) {
            channelManager.addModPermission(Chatter.Permission.COLOR);
        }

        if (config.getBoolean("moderator-permissions.can-view-info")) {
            channelManager.addModPermission(Chatter.Permission.INFO);
        }

        if (config.getBoolean("moderator-permissions.can-focus")) {
            channelManager.addModPermission(Chatter.Permission.FOCUS);
        }

        if (channelManager.getChannels().isEmpty()) {
            StandardChannel defaultChannel = new StandardChannel(channelManager.getStorage(), "Global", "G",
                    channelManager);
            defaultChannel.setColor(ChatColor.DARK_GREEN);
            channelManager.addChannel(defaultChannel);
        }

        String defaultChannel1 = config.getString("default-channel");
        if (defaultChannel1 != null && channelManager.hasChannel(defaultChannel1)) {
            channelManager.setDefaultChannel(channelManager.getChannel(defaultChannel1));
        }

        SurvivorBot.getMessageHandler().setCensors(config.getStringList("censors"));
        channelManager.setStandardFormat(config.getString("format.default"));
        channelManager.setAnnounceFormat(config.getString("format.announce"));
        channelManager.setDiscordFormat(config.getString("format.discord"));
        channelManager.setEmoteFormat(config.getString("format.emote"));
        channelManager.setConversationFormat(config.getString("format.private-message"));
        channelManager.setUsingEmotes(config.getBoolean("use-channel-emotes", true));
        try {
            SurvivorBot.setLocale(this.loadLocale(config.getString("locale")));
        } catch (ClassNotFoundException e) {
            SurvivorBot.info("Unable to load translation information.");
            Bukkit.getPluginManager().disablePlugin(SurvivorBot.getPlugin());
            e.printStackTrace();
        }
        SurvivorBot.setChatLogEnabled(config.getBoolean("log-chat", true));
        SurvivorBot.setLogToBukkitEnabled(config.getBoolean("log-to-bukkit", false));
        SurvivorBot.getMessageHandler().setTwitterStyleMsgs(config.getBoolean("twitter-style-private-messages", true));

        SurvivorBot.getInstance().setMaster(config.getBoolean("discord.master"));
        SurvivorBot.getInstance().setToken(config.getString("discord.bot-token"));
        SurvivorBot.getInstance().setAdminRankID(config.getString("discord.admin-rank-id"));
        SurvivorBot.getInstance().setServerID(config.getString("discord.server-id"));
        SurvivorBot.getInstance().setInviteURL(config.getString("discord.inviteURL"));

        SurvivorBot.getJedisManager().setHostname(config.getString("redis.hostname"));
        SurvivorBot.getJedisManager().setPort(config.getInt("redis.port"));
        SurvivorBot.getJedisManager().setPassword(config.getString("redis.password"));

        try {
            config.options().copyDefaults(true);
            config.save(file);
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }

    private Locale loadLocale(String locale) {
        int index;
        if (locale.contains("_")) {
            index = locale.indexOf("_");
            return new Locale(locale.substring(0, index), locale.substring(index + 1));
        } else if (locale.contains("-")) {
            index = locale.indexOf("-");
            return new Locale(locale.substring(0, index), locale.substring(index + 1));
        } else {
            return new Locale(locale);
        }
    }

    private MemoryConfiguration getDefaults() {
        MemoryConfiguration config = new MemoryConfiguration();
        config.set("moderator-permissions.can-kick", Boolean.TRUE);
        config.set("moderator-permissions.can-ban", Boolean.TRUE);
        config.set("moderator-permissions.can-mute", Boolean.TRUE);
        config.set("moderator-permissions.can-remove-channel", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-nick", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-color", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-distance", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-password", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-format", Boolean.FALSE);
        config.set("moderator-permissions.can-modify-shortcut", Boolean.FALSE);
        config.set("moderator-permissions.can-modify-verbose", Boolean.TRUE);
        config.set("moderator-permissions.can-modify-focusable", Boolean.FALSE);
        config.set("moderator-permissions.can-modify-crossworld", Boolean.FALSE);
        config.set("moderator-permissions.can-color-messages", Boolean.TRUE);
        config.set("moderator-permissions.can-view-info", Boolean.TRUE);
        config.set("moderator-permissions.can-focus", Boolean.TRUE);
        config.set("default-channel", "Global");
        config.set("censors", new ArrayList());
        config.set("format.default", SurvivorBot.getChannelManager().getStandardFormat());
        config.set("format.announce", SurvivorBot.getChannelManager().getAnnounceFormat());
        config.set("format.discord", SurvivorBot.getChannelManager().getDiscordFormat());
        config.set("format.emote", SurvivorBot.getChannelManager().getEmoteFormat());
        config.set("format.private-message", SurvivorBot.getChannelManager().getConversationFormat());
        config.set("use-channel-emotes", Boolean.TRUE);
        config.set("locale", "en_US");
        config.set("log-chat", Boolean.TRUE);
        config.set("log-to-bukkit", Boolean.FALSE);
        config.set("twitter-style-private-messages", Boolean.TRUE);
        config.set("discord.master", Boolean.TRUE);
        config.set("discord.bot-token", "");
        config.set("discord.admin-rank-id", "");
        config.set("discord.server-id", "");
        config.set("discord.inviteURL", "");
        config.set("redis.hostname", "");
        config.set("redis.port", 6379);
        config.set("redis.password", "");
        return config;
    }
}










