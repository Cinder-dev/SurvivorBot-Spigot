package com.fireblazerrr.survivorbot.chatter;

import com.fireblazerrr.survivorbot.channel.Channel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public interface Chatter {

    boolean addChannel(Channel channel, boolean announce, boolean flag);

    Chatter.Result canBan(Channel channel);

    Chatter.Result canColorMessage(Channel channel, ChatColor color);

    Chatter.Result canEmote(Channel channel);

    Chatter.Result canFocus(Channel channel);

    Chatter.Result canJoin(Channel channel, String s);

    Chatter.Result canKick(Channel channel);

    Chatter.Result canLeave(Channel channel);

    Chatter.Result canModify(String s, Channel channel);

    Chatter.Result canMute(Channel channel);

    Chatter.Result canRemove(Channel channel);

    Chatter.Result canSpeak(Channel channel);

    Chatter.Result canViewInfo(Channel channel);

    Chatter.Result canIgnore(Chatter chatter);

    Channel getActiveChannel();

    String getAFKMessage();

    void setAFKMessage(String message);

    Set<Channel> getChannels();

    Set<String> getIgnores();

    Channel getLastActiveChannel();

    Channel getLastFocusableChannel();

    Chatter getLastPrivateMessageSource();

    void setLastPrivateMessageSource(Chatter chatter);

    String getName();

    Player getPlayer();

    boolean hasChannel(Channel channel);

    boolean isAFK();

    void setAFK(boolean afk);

    boolean isIgnoring(Chatter chatter);

    boolean isIgnoring(String chatter);

    boolean isInRange(Chatter chatter, int range);

    boolean isMuted();

    boolean removeChannel(Channel channel, boolean announce, boolean flag);

    void setActiveChannel(Channel channel, boolean announce, boolean flag);

    void setIgnore(String name, boolean ignore, boolean flag);

    void setMuted(boolean muted, boolean flag);

    boolean shouldAutoJoin(Channel channel);

    boolean shouldForceJoin(Channel channel);

    boolean shouldForceLeave(Channel channel);

    void refocus();

    void disconnect();

    Team getTeam();

    void setTeam(Team t);

    enum Result {
        NO_PERMISSION,
        NO_CHANNEL,
        INVALID,
        BANNED,
        MUTED,
        ALLOWED,
        BAD_WORLD,
        BAD_PASSWORD,
        FAIL;

        Result() {

        }

    }

    enum Permission {
        JOIN("join"),
        LEAVE("leave"),
        SPEAK("speak"),
        EMOTE("emote"),
        KICK("kick"),
        BAN("ban"),
        MUTE("mute"),
        REMOVE("remove"),
        COLOR("color.all"),
        INFO("info"),
        FOCUS("focus"),
        AUTOJOIN("autojoin"),
        FORCE_JOIN("force.join"),
        FORCE_LEAVE("force.leave"),
        MODIFY_NICK("modify.nick"),
        MODIFY_COLOR("modify.color"),
        MODIFY_DISTANCE("modify.distance"),
        MODIFY_FORMAT("modify.format"),
        MODIFY_SHORTCUT("modify.shortcut"),
        MODIFY_PASSWORD("modify.password"),
        MODIFY_VERBOSE("modify.verbose"),
        MODIFY_FOCUSABLE("modify.focusable"),
        MODIFY_CROSSWORLD("modify.crossworld"),
        MODIFY_CHATCOST("modify.chatcost"),
        BLACK("color.black"),
        DARK_BLUE("color.dark_blue"),
        DARK_GREEN("color.dark_green"),
        DARK_AQUA("color.dark_aqua"),
        DARK_RED("color.dark_red"),
        DARK_PURPLE("color.dark_purple"),
        GOLD("color.gold"),
        GRAY("color.gray"),
        DARK_GRAY("color.dark_gray"),
        BLUE("color.blue"),
        GREEN("color.green"),
        AQUA("color.aqua"),
        RED("color.red"),
        LIGHT_PURPLE("color.light_purple"),
        YELLOW("color.yellow"),
        WHITE("color.white"),
        MAGIC("color.magic"),
        BOLD("color.bold"),
        STRIKETHROUGH("color.strikethrough"),
        UNDERLINE("color.underline"),
        ITALIC("color.italic"),
        RESET("color.reset");

        private String name;

        Permission(String name) {
            this.name = name;
        }

        public String form(Channel channel) {
            return "survivorbot." + this.name + "." + channel.getName();
        }

        public String formAll() {
            return "survivorbot." + this.name + ".all";
        }

        public String formWildcard() {
            return "survivorbot." + this.name + ".*";
        }

        public String toString() {
            return this.name;
        }
    }
}
