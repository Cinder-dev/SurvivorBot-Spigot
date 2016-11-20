package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChannelChatEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface Channel {

    boolean addMember(Chatter chatter, boolean announce, boolean flag);

    void addWorld(String world);

    void announce(String message);

    String applyFormat(String format, String originalFormat);

    String applyFormat(String s1, String s2, Player player);

    boolean banMember(Chatter chatter, boolean bool, boolean flag);

    void emote(Chatter chatter, String message);

    Set<String> getBans();

    ChatColor getColor();

    int getDistance();

    String getFormat();

    Set<Chatter> getMembers();

    Set<String> getModerators();

    Set<String> getMutes();

    String getName();

    String getNick();

    String getPassword();

    Set<String> getWorlds();

    boolean hasWorld(String world);

    boolean hasWorld(World world);

    boolean isBanned(String s);

    boolean isCrossWorld();

    boolean isHidden();

    boolean isLocal();

    boolean isMember(Chatter chatter);

    boolean isModerator(String s);

    boolean isMuted(String s);

    boolean isShortcutAllowed();

    boolean isTransient();

    boolean isVerbose();

    boolean kickMember(Chatter chatter, boolean bool);

    void onFocusGain(Chatter chatter);

    void onFocusLoss(Chatter chatter);

    void processChat(ChannelChatEvent event);

    boolean removeMember(Chatter chatter, boolean announce, boolean flag);

    void removeWorld(String world);

    void setBanned(String chatter, boolean bool);

    void setBans(Set<String> bans);

    void setColor(ChatColor color);

    void setCrossWorld(boolean bool);

    void setDistance(int distance);

    void setFormat(String format);

    void setModerator(String chatter, boolean bool);

    void setModerators(Set<String> chatters);

    void setMuted(String chatter, boolean bool);

    void setMutes(Set<String> chatters);

    void setNick(String nick);

    void setPassword(String password);

    void setShortcutAllowed(boolean bool);

    void setVerbose(boolean bool);

    void setWorlds(Set<String> worlds);

    void setMuted(boolean bool);

    boolean isMuted();

    void sendRawMessage(String message);

    MessageFormatSupplier getFormatSupplier();
}
