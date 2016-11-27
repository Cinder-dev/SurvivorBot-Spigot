package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter.Permission;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.*;
import java.util.stream.Collectors;

public class ChannelManager implements MessageFormatSupplier {
    private Map<String, Channel> channels = new HashMap<>();
    private Channel defaultChannel;
    private Map<Permission, org.bukkit.permissions.Permission> wildcardPermissions = new EnumMap<>(Permission.class);
    private Set<Permission> modPermissions = EnumSet.noneOf(Permission.class);
    private ChannelStorage storage;
    private String standardFormat = "{color}[{nick}{color}] &f{prefix}{sender}{suffix}{color}: {msg}";
    private String emoteFormat = "{color}[{nick}{color}] * {msg}";
    private String announceFormat = "{color}[{nick}{color}] {msg}";
    private String discordFormat = "&9<Discord:{sender}&9>";
    private String conversationFormat = "&d{convoaddress} {convopartner}: {msg}";
    private boolean usingEmotes;

    public ChannelManager() {
        this.registerChannelPermissions();
    }

    /**
     * Add a channel to the {@link ChannelManager}
     *
     * @param channel Channel to add.
     */
    public void addChannel(Channel channel) {
        this.channels.put(channel.getName().toLowerCase(), channel);
        this.channels.put(channel.getNick().toLowerCase(), channel);
        if (!channel.isTransient()) {
            Permission[] pm = Permission.values();

            for (Permission forceJoinPermission : pm) {
                org.bukkit.permissions.Permission forceLeavePermission = this.wildcardPermissions
                        .get(forceJoinPermission);
                forceLeavePermission.getChildren().put(forceJoinPermission.form(channel).toLowerCase(), Boolean.TRUE);
                forceLeavePermission.recalculatePermissibles();
            }

            PluginManager pluginManager = Bukkit.getServer().getPluginManager();
            String focus = Permission.FOCUS.form(channel).toLowerCase();

            try {
                pluginManager.addPermission(new org.bukkit.permissions.Permission(focus, PermissionDefault.TRUE));
            } catch (IllegalArgumentException ignored) {
            }

            String autoJoin = Permission.AUTOJOIN.form(channel).toLowerCase();

            try {
                pluginManager.addPermission(new org.bukkit.permissions.Permission(autoJoin, PermissionDefault.FALSE));
            } catch (IllegalArgumentException ignored) {
            }

            String forceJoin = Permission.FORCE_JOIN.form(channel).toLowerCase();

            try {
                pluginManager.addPermission(new org.bukkit.permissions.Permission(forceJoin, PermissionDefault.FALSE));
            } catch (IllegalArgumentException ignored) {

            }

            String forceLeave = Permission.FORCE_LEAVE.form(channel).toLowerCase();

            try {
                pluginManager.addPermission(new org.bukkit.permissions.Permission(forceLeave, PermissionDefault.FALSE));
            } catch (IllegalArgumentException ignored) {

            }

            if (this.defaultChannel == null) {
                this.defaultChannel = channel;
            }

            this.storage.addChannel(channel);
        }
    }

    /**
     * Adds a {@link Permission} to the available moderator permissions.
     *
     * @param permission {@link Permission} to add.
     */
    public void addModPermission(Permission permission) {
        this.modPermissions.add(permission);
    }

    /**
     * Checks if moderators have access to a {@link Permission}
     *
     * @param permission {@link Permission} to check for.
     *
     * @return true if {@link Permission} is set.
     */
    public boolean checkModPermission(Permission permission) {
        return this.modPermissions.contains(permission);
    }

    /**
     * Resets the {@link ChannelManager} to default state.
     */
    public void clear() {
        this.defaultChannel = null;
        this.channels.clear();

        this.modPermissions.clear();
        this.storage = null;
        this.standardFormat = "{color}[{nick}{color}] " + ChatColor.WHITE + "{prefix}{sender}{suffix}{color}: {msg}";
        this.announceFormat = "{color}[{nick}{color}] {msg}";
        this.emoteFormat = "{color}[{nick}{color}] * {msg}";
        this.discordFormat = "&9<Discord:{sender}&9>";
        this.conversationFormat = ChatColor.LIGHT_PURPLE + "{convoaddress} {convopartner}: {msg}";
    }

    /**
     * Get a {@link Channel} from the {@link ChannelManager} using the name or nick of the channel.
     *
     * @param identifier String with the channel name or nick to return.
     *
     * @return {@link Channel} is returned if available.
     */
    public Channel getChannel(String identifier) {
        return this.channels.get(identifier.toLowerCase());
    }

    /**
     * Get a {@link List} of {@link Channel}.
     *
     * @return a {@link List} of {@link Channel}.
     */
    public List<Channel> getChannels() {
        SurvivorBot.debug("Channel Identifiers", this.channels.keySet().stream().map(String::toString).collect(Collectors.toList()).toString());
        ArrayList<Channel> list = new ArrayList<>();

        this.channels.values().stream().filter(channel -> !list.contains(channel)).forEach(list::add);

        return list;
    }

    public Channel getDefaultChannel() {
        return this.defaultChannel;
    }

    public void setDefaultChannel(Channel channel) {
        this.defaultChannel = channel;
    }

    public Set<Permission> getModPermissions() {
        return this.modPermissions;
    }

    public void setModPermissions(Set<Permission> modPermissions) {
        this.modPermissions = modPermissions;
    }

    public String getStandardFormat() {
        return this.standardFormat;
    }

    public String getConversationFormat() {
        return this.conversationFormat;
    }

    public String getAnnounceFormat() {
        return this.announceFormat;
    }

    public String getEmoteFormat() {
        return this.emoteFormat;
    }

    public void setEmoteFormat(String emoteFormat) {
        this.emoteFormat = emoteFormat;
    }

    public void setAnnounceFormat(String announceFormat) {
        this.announceFormat = announceFormat;
    }

    public void setConversationFormat(String conversationFormat) {
        this.conversationFormat = conversationFormat;
    }

    public void setStandardFormat(String standardFormat) {
        this.standardFormat = standardFormat;
    }

    public String getDiscordFormat() {
        return this.discordFormat;
    }

    public void setDiscordFormat(String discordFormat) {
        this.discordFormat = discordFormat;
    }

    public ChannelStorage getStorage() {
        return this.storage;
    }

    public void setStorage(ChannelStorage storage) {
        this.storage = storage;
    }

    public boolean isUsingEmotes() {
        return this.usingEmotes;
    }

    public void setUsingEmotes(boolean usingEmotes) {
        this.usingEmotes = usingEmotes;
    }

    public boolean hasChannel(String identifier) {
        return this.channels.containsKey(identifier.toLowerCase());
    }

    public void loadChannels() {
        this.storage.loadChannels().forEach(this::addChannel);
    }

    public void registerChannelPermissions() {
        Arrays.stream(Permission.values()).forEach(permission -> {
            org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(permission.formWildcard(), PermissionDefault.FALSE);
            Bukkit.getServer().getPluginManager().addPermission(perm);
            this.wildcardPermissions.put(permission, perm);
        });
    }

    public void removeChannel(Channel channel) {
        this.channels.remove(channel.getName().toLowerCase());
        this.channels.remove(channel.getNick().toLowerCase());
        if (!channel.isTransient()) {
            Arrays.stream(Permission.values()).forEach(permission -> {
                org.bukkit.permissions.Permission forceLeavePermission = this.wildcardPermissions.get(permission);
                forceLeavePermission.getChildren().remove(permission.form(channel).toLowerCase());
                forceLeavePermission.recalculatePermissibles();
            });

            PluginManager pluginManager = Bukkit.getServer().getPluginManager();
            String focus = Permission.FOCUS.form(channel).toLowerCase();
            pluginManager.removePermission(focus);
            String autoJoin = Permission.AUTOJOIN.form(channel).toLowerCase();
            pluginManager.removePermission(autoJoin);
            String forceJoin = Permission.FORCE_JOIN.form(channel).toLowerCase();
            pluginManager.removePermission(forceJoin);
            String forceLeave = Permission.FORCE_LEAVE.form(channel).toLowerCase();
            pluginManager.removePermission(forceLeave);

            this.storage.removeChannel(channel);
        }
    }
}
