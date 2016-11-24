package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChannelChatEvent;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChatCompleteEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StandardChannel implements Channel {
    private static final Pattern msgPattern = Pattern.compile("(.*)<(.*)%1\\$s(.*)> $2\\$s");
    private final String name;
    private String nick;
    private String format;
    private String password;
    private String discordChannelLinkID;
    private ChatColor color;
    private int distance;
    private boolean shortcutAllowed;
    private boolean verbose;
    private boolean crossWorld;
    private boolean muted;
    private Set<Chatter> members = new HashSet<>();
    private Set<String> worlds = new HashSet<>();
    private Set<String> bans = new HashSet<>();
    private Set<String> moderators = new HashSet<>();
    private Set<String> mutes = new HashSet<>();
    private ChannelStorage storage;
    private final MessageFormatSupplier formatSupplier;

    public StandardChannel(ChannelStorage storage, String name, String nick, MessageFormatSupplier formatSupplier) {
        this.storage = storage;
        this.name = name;
        this.nick = nick;
        this.color = ChatColor.WHITE;
        this.distance = 0;
        this.shortcutAllowed = false;
        this.verbose = true;
        this.format = "{default}";
        this.password = "";
        this.discordChannelLinkID = "";
        this.formatSupplier = formatSupplier;
        this.muted = false;
    }

    public boolean addMember(Chatter chatter, boolean announce, boolean flagUpdate) {
        if (this.members.contains(chatter)) {
            return false;
        } else {
            if (announce && this.verbose) {
                try {
                    this.announce(
                            SurvivorBot.getMessage("channel_join").replace("$1", chatter.getPlayer().getDisplayName()));
                } catch (MessageNotFoundException e) {
                    SurvivorBot.severe("Message.properties is missing: channel_join");
                }
            }

            this.members.add(chatter);
            if (!chatter.hasChannel(this)) {
                chatter.addChannel(this, announce, flagUpdate);
            }

            return true;
        }
    }

    public void addWorld(String world) {
        if (!this.worlds.contains(world)) {
            this.worlds.add(world);
            this.storage.flagUpdate(this);
        }
    }

    public void announce(String message) {
        String colorized = ChatColor.translateAlternateColorCodes('&', message);
        message = this.applyFormat(this.formatSupplier.getAnnounceFormat(), "").replace("%2$s", colorized);

        String finalMessage = message;
        this.members.forEach(chatter -> chatter.getPlayer().sendMessage(finalMessage));

        SurvivorBot.logChat(ChatColor.stripColor(message));
    }

    public void sendRawMessage(String message) {
        this.members.forEach(chatter -> chatter.getPlayer().sendMessage(message));
    }

    public String applyFormat(String format, String originalFormat) {
        format = format.replace("{default}", this.formatSupplier.getStandardFormat())
                .replace("{name}", this.name)
                .replace("{nick}", this.nick)
                .replace("{color}", this.color.toString())
                .replace("{msg}", "%2$s");
        Matcher matcher = msgPattern.matcher(originalFormat);
        format = (matcher.matches() && matcher.groupCount() == 3) ?
                format.replace("{sender}", matcher.group(1) + matcher.group(2) + "%1$s" + matcher.group(3)) :
                format.replace("{sender}", "%1$s");

        format = ChatColor.translateAlternateColorCodes('&', format);
        return format;
    }

    public String applyFormat(String format, String originalFormat, Player sender) {
        format = this.applyFormat(format, originalFormat)
                .replace("{plainsender}", sender.getName())
                .replace("{world}", sender.getWorld().getName());

        Chatter chatter = SurvivorBot.getChatterManager().getChatter(sender);
        format = format.replace("{prefix}", chatter.getTeam() == null ? "" : chatter.getTeam().getPrefix())
                .replace("{suffix}", chatter.getTeam() == null ? "" : chatter.getTeam().getSuffix())
                .replace("{group}", chatter.getTeam() == null ? "" : chatter.getTeam().getName())
                .replace("{groupprefix}", "")
                .replace("{groupsuffix}", "");


        format = ChatColor.translateAlternateColorCodes('&', format);
        return format;
    }

    @Override
    public boolean banMember(Chatter chatter, boolean bool, boolean flag) {
        return false;
    }

    public void attachStorage(ChannelStorage storage) {
        this.storage = storage;
    }

    public boolean banMember(Chatter chatter, boolean announce) {
        if (!this.members.contains(chatter)) {
            return false;
        } else {
            this.removeMember(chatter, false, true);
            this.setBanned(chatter.getPlayer().getName(), true);
            if (announce) {
                try {
                    this.announce(
                            SurvivorBot.getMessage("channel_ban").replace("$1", chatter.getPlayer().getDisplayName()));
                } catch (MessageNotFoundException e) {
                    SurvivorBot.severe("Messages.properties is missing: channel_ban");
                }
            }

            return true;
        }
    }

    public void emote(Chatter sender, String message) {
        message = this.applyFormat(this.formatSupplier.getEmoteFormat(), "").replace("%2$s", message);

        Set<Player> recipients = this.members.stream().map(Chatter::getPlayer).collect(Collectors.toSet());

        this.trimRecipients(recipients, sender);

        String finalMessage = message;
        recipients.stream().forEach(player -> player.sendMessage(finalMessage));

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(sender, this, message));
        SurvivorBot.logChat(message);
    }

    public boolean equals(Object other) {
        if (other == this)
            return true;
        else if (other == null)
            return false;
        else if (!(other instanceof Channel))
            return false;
        else {
            Channel channel = (Channel) other;
            return this.name.equalsIgnoreCase(channel.getName()) || this.name.equalsIgnoreCase(channel.getNick());
        }
    }

    public Set<String> getBans() {
        return new HashSet(this.bans);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public int getDistance() {
        return this.distance;
    }

    public String getFormat() {
        return this.format;
    }

    public Set<Chatter> getMembers() {
        return new HashSet(this.members);
    }

    public Set<String> getModerators() {
        return new HashSet(this.moderators);
    }

    public Set<String> getMutes() {
        return new HashSet(this.mutes);
    }

    public String getName() {
        return this.name;
    }

    public String getNick() {
        return this.nick;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDiscordChannelLinkID() {
        return this.discordChannelLinkID;
    }

    public ChannelStorage getStorage() {
        return this.storage;
    }

    public Set<String> getWorlds() {
        return new HashSet(this.worlds);
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.name == null ? 0 : this.name.toLowerCase().hashCode());
        result1 = 31 * result1 + (this.nick == null ? 0 : this.nick.toLowerCase().hashCode());
        return result1;
    }

    public boolean hasWorld(String world) {
        return this.worlds.isEmpty() || this.worlds.contains(world);
    }

    public boolean hasWorld(World world) {
        return this.worlds.isEmpty() || this.worlds.contains(world.getName());
    }

    public boolean isBanned(String name) {
        return this.bans.contains(name.toLowerCase());
    }

    public boolean isCrossWorld() {
        return this.crossWorld;
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isLocal() {
        return this.distance != 0;
    }

    public boolean isMember(Chatter chatter) {
        return this.members.contains(chatter);
    }

    public boolean isModerator(String name) {
        return this.moderators.contains(name.toLowerCase());
    }

    public boolean isMuted(String name) {
        return this.muted || this.mutes.contains(name.toLowerCase());
    }

    public boolean isShortcutAllowed() {
        return this.shortcutAllowed;
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public boolean kickMember(Chatter chatter, boolean bool, boolean flag) {
        return false;
    }

    public boolean kickMember(Chatter chatter, boolean announce) {
        if (!this.members.contains(chatter)) {
            return false;
        } else {
            this.removeMember(chatter, false, true);
            if (announce) {
                try {
                    this.announce(
                            SurvivorBot.getMessage("channel_kick").replace("$1", chatter.getPlayer().getDisplayName()));
                } catch (MessageNotFoundException ex) {
                    SurvivorBot.severe("Messages.properties is missing: channel_kick");
                }
            }

            return true;
        }
    }

    public void onFocusGain(Chatter chatter) {
    }

    public void onFocusLoss(Chatter chatter) {
    }

    public void processChat(ChannelChatEvent event) {
        Player player = event.getSender().getPlayer();
        String format = this.applyFormat(event.getFormat(), event.getBukkitFormat(), player);
        Chatter sender = SurvivorBot.getChatterManager().getChatter(player);
        Set<Player> recipients = Bukkit.getOnlinePlayers().stream().collect(Collectors.toSet());

        this.trimRecipients(recipients, sender);

        String msg = String.format(format, player.getName(), event.getMessage());

        TextComponent root = new TextComponent(msg);
        root.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.getName()));

        // Send to players in channel

        recipients.forEach(player1 -> player1.spigot().sendMessage(root));

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(sender, this, msg));

        // Send to discord
        if (!discordChannelLinkID.equals("")) {
            try {
                SurvivorBot.getInstance().getClient().getChannelByID(discordChannelLinkID)
                        .sendMessage("<" + player.getName() + "> " + event.getMessage());
            } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
                e.printStackTrace();
            }
        }
        SurvivorBot.logChat(msg);
    }

    public boolean removeMember(Chatter chatter, boolean announce, boolean flagUpdate) {
        if (!this.members.contains(chatter)) {
            return false;
        } else {
            this.members.remove(chatter);
            if (chatter.hasChannel(this)) {
                chatter.removeChannel(this, announce, flagUpdate);
            }

            if (announce && this.verbose) {
                try {
                    this.announce(SurvivorBot.getMessage("channel_leave")
                            .replace("$1", chatter.getPlayer().getDisplayName()));
                } catch (MessageNotFoundException var5) {
                    SurvivorBot.severe("Messages.properties is missing: channel_leave");
                }
            }

            return true;
        }
    }

    public void removeWorld(String world) {
        if (this.worlds.contains(world)) {
            this.worlds.remove(world);
            this.storage.flagUpdate(this);
        }

    }

    public void setBanned(String name, boolean banned) {
        if (banned) {
            this.bans.add(name.toLowerCase());
        } else {
            this.bans.remove(name.toLowerCase());
        }

        this.storage.flagUpdate(this);
    }

    public void setBans(Set<String> bans) {
        this.bans = bans;
        this.storage.flagUpdate(this);
    }

    public void setColor(ChatColor color) {
        this.color = color;
        this.storage.flagUpdate(this);
    }

    public void setCrossWorld(boolean crossWorld) {
        this.crossWorld = crossWorld;
        this.storage.flagUpdate(this);
    }

    public void setDistance(int distance) {
        this.distance = distance < 0 ? 0 : distance;
        this.storage.flagUpdate(this);
    }

    public void setFormat(String format) {
        this.format = format;
        this.storage.flagUpdate(this);
    }

    public void setModerator(String name, boolean moderator) {
        if (moderator) {
            this.moderators.add(name.toLowerCase());
        } else {
            this.moderators.remove(name.toLowerCase());
        }

        this.storage.flagUpdate(this);
    }

    public void setModerators(Set<String> moderators) {
        this.moderators = moderators;
        this.storage.flagUpdate(this);
    }

    public void setMuted(boolean value) {
        this.muted = value;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public void setMuted(String name, boolean muted) {
        if (muted) {
            this.mutes.add(name.toLowerCase());
        } else {
            this.mutes.remove(name.toLowerCase());
        }

        this.storage.flagUpdate(this);
    }

    public void setMutes(Set<String> mutes) {
        this.mutes = mutes;
        this.storage.flagUpdate(this);
    }

    public void setNick(String nick) {
        this.nick = nick;
        this.storage.flagUpdate(this);
    }

    public void setPassword(String password) {
        if (password == null) {
            this.password = "";
        } else {
            this.password = password;
        }

        this.storage.flagUpdate(this);
    }

    public void setDiscordChannelLinkID(String discordChannelLinkID) {
        this.discordChannelLinkID = discordChannelLinkID;
    }

    public void setShortcutAllowed(boolean shortcutAllowed) {
        this.shortcutAllowed = shortcutAllowed;
        this.storage.flagUpdate(this);
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
        this.storage.flagUpdate(this);
    }

    public void setWorlds(Set<String> worlds) {
        this.worlds = worlds;
        this.storage.flagUpdate(this);
    }

    private boolean isMessageHeard(Set<Player> recipients, Chatter sender) {
        if (!this.isLocal()) {
            return true;
        } else {
            Player senderPlayer = sender.getPlayer();
            int visibleRecipients = recipients.stream()
                    .filter(player -> player.hasPermission("survivorbot.admin.stealth") && !player.equals(senderPlayer))
                    .collect(Collectors.toList()).size();

            return visibleRecipients > 0;
        }
    }

    private void trimRecipients(Set<Player> recipients, Chatter sender) {
        World world = sender.getPlayer().getWorld();

        Set<Player> toRemove = recipients.stream()
                .map(player -> SurvivorBot.getChatterManager().getChatter(player))
                .filter(chatter -> !this.members.contains(chatter)
                        || this.isLocal() && !sender.isInRange(chatter, this.distance)
                        || !this.hasWorld(chatter.getPlayer().getWorld())
                        || chatter.isIgnoring(sender)
                        || !this.crossWorld && !world.equals(chatter.getPlayer().getWorld()))
                .map(Chatter::getPlayer)
                .collect(Collectors.toSet());
        recipients.removeAll(toRemove);

    }

    public MessageFormatSupplier getFormatSupplier() {
        return this.formatSupplier;
    }
}































