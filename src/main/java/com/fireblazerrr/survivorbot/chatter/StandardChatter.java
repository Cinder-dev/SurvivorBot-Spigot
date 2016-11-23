package com.fireblazerrr.survivorbot.chatter;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class StandardChatter implements Chatter {
    private final Player player;
    private Chatter lastPMSource;
    private Channel activeChannel;
    private Channel lastActiveChannel;
    private Channel lastFocusableChannel;
    private ChatterStorage storage;
    private Set<Channel> channels = new HashSet<>();
    private Set<String> ignores = new HashSet<>();
    private String afkMessage = "";
    private boolean muted = false;
    private boolean afk = false;

    StandardChatter(ChatterStorage storage, Player player) {
        this.storage = storage;
        this.player = player;
    }

    public boolean addChannel(Channel channel, boolean announce, boolean flagUpdate) {
        if (this.channels.contains(channel)) {
            return false;
        } else {
            this.channels.add(channel);
            if (!channel.isMember(this)) {
                channel.addMember(this, announce, flagUpdate);
            }

            if (flagUpdate) {
                this.storage.flagUpdate(this);
            }

            return true;
        }
    }

    public void attachStorage(ChatterStorage storage) {
        this.storage = storage;
    }

    public Result canBan(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.BAN) ?
                Result.ALLOWED :
                (channel.isModerator(this.player.getName()) && SurvivorBot.getChannelManager()
                        .checkModPermission(Permission.BAN) ?
                        Result.ALLOWED :
                        Result.NO_PERMISSION);
    }

    @Override
    public Result canColorMessage(Channel channel, ChatColor color) {
        return null;
    }

    public Result canColorMessages(Channel channel, ChatColor color) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.COLOR) ? Result.ALLOWED :
                (!SurvivorBot.hasChannelPermission(this.player, channel, Permission.valueOf(color.name())) ?
                        Result.NO_PERMISSION : Result.ALLOWED);
    }

    public Result canEmote(Channel channel) {
        return channel == null ? Result.NO_CHANNEL : (!channel.isMember(this) ? Result.INVALID :
                (!channel.isTransient() && SurvivorBot.hasChannelPermission(this.player, channel, Permission.EMOTE) ?
                        (!this.muted && !channel.isMuted(this.player.getName()) ?
                                (!channel.hasWorld(this.player.getWorld()) ? Result.BAD_WORLD : Result.ALLOWED) :
                                Result.MUTED) : Result.NO_PERMISSION));
    }

    public Result canFocus(Channel channel) {
        if (!SurvivorBot.hasChannelPermission(this.player, channel, Permission.FOCUS)) {
            return Result.NO_PERMISSION;
        } else {
            Result speak = this.canSpeak(channel);
            return speak != Result.ALLOWED && speak != Result.INVALID ? Result.NO_PERMISSION : Result.ALLOWED;
        }
    }

    public Result canJoin(Channel channel, String password) {
        return channel.isMember(this) ? Result.INVALID :
                (!SurvivorBot.hasChannelPermission(this.player, channel, Permission.JOIN) ? Result.NO_PERMISSION :
                        (channel.isBanned(this.player.getName()) ? Result.BANNED :
                                (!password.equals(channel.getPassword()) ? Result.BAD_PASSWORD : Result.ALLOWED)));
    }

    public Result canKick(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.KICK) ? Result.ALLOWED :
                (channel.isModerator(this.player.getName()) && SurvivorBot.getChannelManager()
                        .checkModPermission(Permission.KICK) ? Result.ALLOWED : Result.NO_PERMISSION);
    }

    public Result canLeave(Channel channel) {
        return !channel.isMember(this) ? Result.INVALID :
                (!SurvivorBot.hasChannelPermission(this.player, channel, Permission.LEAVE) ? Result.NO_PERMISSION :
                        Result.ALLOWED);
    }

    public Result canModify(String setting, Channel channel) {
        setting = setting.toLowerCase();
        Permission permission;
        switch (setting) {
            case "nick":
                permission = Permission.MODIFY_NICK;
                break;
            case "format":
                permission = Permission.MODIFY_FORMAT;
                break;
            case "distance":
                permission = Permission.MODIFY_DISTANCE;
                break;
            case "color":
                permission = Permission.MODIFY_COLOR;
                break;
            case "shortcut":
                permission = Permission.MODIFY_SHORTCUT;
                break;
            case "password":
                permission = Permission.MODIFY_PASSWORD;
                break;
            case "verbose":
                permission = Permission.MODIFY_VERBOSE;
                break;
            case "chatcost":
                permission = Permission.MODIFY_CHATCOST;
                break;
            case "crossworld":
                permission = Permission.MODIFY_CROSSWORLD;
                break;
            default:
                if (!setting.equals("focusable")) {
                    return Result.INVALID;
                }

                permission = Permission.MODIFY_FOCUSABLE;
                break;
        }

        return SurvivorBot.hasChannelPermission(this.player, channel, permission) ? Result.ALLOWED :
                (channel.isModerator(this.player.getName()) && SurvivorBot.getChannelManager()
                        .checkModPermission(permission) ? Result.ALLOWED : Result.NO_PERMISSION);
    }

    public Result canMute(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.MUTE) ? Result.ALLOWED :
                (channel.isModerator(this.player.getName()) && SurvivorBot.getChannelManager()
                        .checkModPermission(Permission.BAN) ? Result.ALLOWED : Result.NO_PERMISSION);
    }

    public Result canRemove(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.REMOVE) ? Result.ALLOWED :
                (channel.isModerator(this.player.getName()) && SurvivorBot.getChannelManager()
                        .checkModPermission(Permission.REMOVE) ? Result.ALLOWED : Result.NO_PERMISSION);
    }

    public Result canSpeak(Channel channel) {
        return !channel.isMember(this) ? Result.INVALID :
                (!channel.isTransient() && !SurvivorBot.hasChannelPermission(this.player, channel, Permission.SPEAK) ?
                        Result.NO_PERMISSION : (!this.muted && !channel.isMuted(this.player.getName()) ?
                        (!channel.hasWorld(this.player.getWorld()) ? Result.BAD_WORLD : Result.ALLOWED) :
                        Result.MUTED));
    }

    public Result canViewInfo(Channel channel) {
        return !SurvivorBot.hasChannelPermission(this.player, channel, Permission.INFO) ? Result.NO_PERMISSION :
                Result.ALLOWED;
    }

    public boolean equals(Object other) {
        return other == this || (other != null && (other instanceof Chatter && this.player
                .equals(((Chatter) other).getPlayer())));
    }

    public Channel getActiveChannel() {
        return this.activeChannel;
    }

    public String getAFKMessage() {
        return this.afkMessage;
    }

    public Set<Channel> getChannels() {
        return this.channels;
    }

    public Set<String> getIgnores() {
        return this.ignores;
    }

    public Channel getLastActiveChannel() {
        return this.lastActiveChannel;
    }

    public Channel getLastFocusableChannel() {
        return this.lastFocusableChannel;
    }

    public Chatter getLastPrivateMessageSource() {
        return this.lastPMSource;
    }

    public String getName() {
        return this.player.getName();
    }

    public Player getPlayer() {
        return this.player;
    }

    public ChatterStorage getStorage() {
        return this.storage;
    }

    public boolean hasChannel(Channel channel) {
        return this.channels.contains(channel);
    }

    public int hashCode() {
        return this.player.hashCode();
    }

    public boolean isAFK() {
        return this.afk;
    }

    public boolean isIgnoring(Chatter other) {
        return this.isIgnoring(other.getName());
    }

    public boolean isIgnoring(String name) {
        return this.ignores.contains(name.toLowerCase());
    }

    public boolean isInRange(Chatter other, int distance) {
        Player otherPlayer = other.getPlayer();
        return this.player.getWorld().equals(otherPlayer.getWorld()) && this.player.getLocation()
                .distanceSquared(otherPlayer.getLocation()) <= (double) (distance * distance);
    }

    public boolean isMuted() {
        return this.muted;
    }

    public boolean removeChannel(Channel channel, boolean announce, boolean flagUpdate) {
        if (!this.channels.contains(channel)) {
            return false;
        } else {
            this.channels.remove(channel);
            if (channel.isMember(this)) {
                channel.removeMember(this, announce, flagUpdate);
            }

            if (flagUpdate) {
                this.storage.flagUpdate(this);
            }

            return true;
        }
    }

    public void setActiveChannel(Channel channel, boolean announce, boolean flagUpdate) {
        if (channel == null || !channel.equals(this.activeChannel)) {
            if (this.activeChannel != null) {
                this.activeChannel.onFocusLoss(this);
            }

            if (this.activeChannel != null && !this.activeChannel.isTransient()) {
                this.lastActiveChannel = this.activeChannel;
                if (this.canFocus(this.activeChannel) == Result.ALLOWED) {
                    this.lastFocusableChannel = this.activeChannel;
                }
            }

            this.activeChannel = channel;
            if (this.activeChannel != null) {
                this.activeChannel.onFocusGain(this);
                if (announce) {
                    try {
                        Messaging.send(this.player, SurvivorBot.getMessage("chatter_focus"),
                                channel.getColor() + channel.getName());
                    } catch (MessageNotFoundException e) {
                        SurvivorBot.severe("Messages.properties is missing: chatter_focus");
                    }
                }
            }

            if (flagUpdate) {
                this.storage.flagUpdate(this);
            }
        }
    }

    public void setAFK(boolean afk) {
        this.afk = afk;
    }

    @Override
    public void setAFKMessage(String message) {

    }

    public void setAfkMessage(String message) {
        this.afkMessage = afkMessage;
    }

    public void setIgnore(String name, boolean ignore, boolean flagUpdate) {
        if (ignore) {
            this.ignores.add(name.toLowerCase());
        } else {
            this.ignores.remove(name.toLowerCase());
        }

        if (flagUpdate) {
            this.storage.flagUpdate(this);
        }
    }

    public void setLastPrivateMessageSource(Chatter chatter) {
        this.lastPMSource = chatter;
    }

    public void setMuted(boolean muted, boolean flagUpdate) {
        this.muted = muted;
        if (flagUpdate) {
            this.storage.flagUpdate(this);
        }
    }

    public boolean shouldAutoJoin(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.AUTOJOIN);
    }

    public boolean shouldForceJoin(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.FORCE_JOIN);
    }

    public boolean shouldForceLeave(Channel channel) {
        return SurvivorBot.hasChannelPermission(this.player, channel, Permission.FORCE_LEAVE);
    }

    public void refocus() {
        if (this.activeChannel != null) {
            if (this.canFocus(this.activeChannel) == Result.ALLOWED) {
                return;
            }

            this.lastActiveChannel = this.activeChannel;
            this.lastActiveChannel.onFocusLoss(this);
            this.activeChannel = null;
        }

        this.getChannels().stream().filter(channel -> canFocus(channel) == Result.ALLOWED).forEach(channel -> {
            this.activeChannel = channel;
            this.activeChannel.onFocusGain(this);

            try {
                Messaging.send(this.player, SurvivorBot.getMessage("chatter_focus"),
                        channel.getColor() + channel.getName());
            } catch (MessageNotFoundException ignored) {
                SurvivorBot.severe("Messages.properties is missing: chatter_focus");
            }
        });

    }

    public Result canIgnore(Chatter other) {
        return other.getPlayer().hasPermission("survivorbot.admin.unignore") ? Result.NO_PERMISSION : Result.ALLOWED;
    }

    public void disconnect() {
        this.channels.forEach(channel -> channel.removeMember(this, false, false));
    }
}











