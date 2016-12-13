package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.events.ChannelChatEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

public interface Channel {

    boolean addMember(Chatter chatter, boolean announce, boolean flag);

    void addWorld(String world);

    void announce(String message);

    void announce(TextComponent textComponent);

    TextComponent applyFormat(String format, String msg, Player player);

    String applyFormat(String format, String originalFormat);

    boolean banMember(Chatter chatter, boolean bool, boolean flag);

    void emote(Chatter chatter, String message);

    Set<String> getBans();

    void setBans(Set<String> bans);

    ChatColor getColor();

    void setColor(ChatColor color);

    int getDistance();

    void setDistance(int distance);

    String getFormat();

    void setFormat(String format);

    Set<Chatter> getMembers();

    Set<String> getModerators();

    void setModerators(Set<String> chatters);

    Set<String> getMutes();

    void setMutes(Set<String> chatters);

    String getName();

    String getNick();

    void setNick(String nick);

    String getPassword();

    void setPassword(String password);

    String getDiscordChannelLinkID();

    void setDiscordChannelLinkID(String discordChannelLinkID);

    Set<String> getWorlds();

    void setWorlds(Set<String> worlds);

    boolean hasWorld(String world);

    boolean hasWorld(World world);

    boolean isBanned(String s);

    boolean isCrossWorld();

    void setCrossWorld(boolean bool);

    boolean isHidden();

    boolean isLocal();

    boolean isMember(Chatter chatter);

    boolean isModerator(String s);

    boolean isMuted(String s);

    boolean isShortcutAllowed();

    void setShortcutAllowed(boolean bool);

    boolean isTransient();

    boolean isVerbose();

    void setVerbose(boolean bool);

    boolean kickMember(Chatter chatter, boolean bool);

    void onFocusGain(Chatter chatter);

    void onFocusLoss(Chatter chatter);

    void processChat(ChannelChatEvent event);

    boolean removeMember(Chatter chatter, boolean announce, boolean flag);

    void removeWorld(String world);

    void setBanned(String chatter, boolean bool);

    void setModerator(String chatter, boolean bool);

    void setMuted(String chatter, boolean bool);

    boolean isMuted();

    void setMuted(boolean bool);

    void sendRawMessage(String message);

    MessageFormatSupplier getFormatSupplier();
}
