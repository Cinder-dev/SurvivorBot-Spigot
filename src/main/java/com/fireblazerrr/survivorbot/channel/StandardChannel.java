package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChannelChatEvent;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChatCompleteEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StandardChannel implements Channel {
    private static final Pattern msgPattern = Pattern.compile("(.*)<(.*)%1\\$s(.*)> $2\\$s");
    private final String name;
    private final MessageFormatSupplier formatSupplier;
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
                    this.announce(SurvivorBot.getMessage("channel_join").replace("$1", chatter.getPlayer().getDisplayName()));
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

    public TextComponent applyFormat(String format, String msg, Player player) {
        Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
        format = format.replace("{default}", this.formatSupplier.getStandardFormat());
        TextComponent root = new TextComponent();

        TextComponent tc = new TextComponent();
        ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.getName());
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
        ChatColor currentColor = ChatColor.WHITE;
        String analyzer = "";
        boolean nextIsColorCode = false;
        boolean inNode = false;
        boolean prefix = false;
        ChatColor playerColor = chatter.getTeam() == null ? ChatColor.WHITE : ChatColor.getByChar(ChatColor.getLastColors(chatter.getTeam().getPrefix()).charAt(1));

        for (char c : format.toCharArray()) {
            if (nextIsColorCode) {
                currentColor = ChatColor.getByChar(c);
                tc.setColor(ChatColor.getByChar(c).asBungee());
                nextIsColorCode = false;
            } else if (c == '{') {
                inNode = true;
            } else if (c == '}') {
                boolean create = false;
                tc = new TextComponent();
                switch (analyzer) {
                    case "name":
                        tc.setText("" + this.name);
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.name);
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
                        create = true;
                        break;
                    case "nick":
                        tc.setText("" + this.nick);
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.name);
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
                        create = true;
                        break;
                    case "color":
                        currentColor = this.color;
                        tc.setColor(this.color.asBungee());
                        break;
                    case "msg":
                        tc.setText("" + msg);
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.name);
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
                        create = true;
                        break;
                    case "sender":
                        if (prefix)
                            tc.setText("" + chatter.getTeam().getPrefix() + player.getName());
                        else
                            tc.setText("" + player.getName());
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "@" + player.getName());
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to msg " + playerColor + player.getName()).create());
                        create = true;
                        break;
                    case "plainsender":
                        tc.setText("" + player.getName());
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "@" + player.getName());
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to msg " + playerColor + player.getName()).create());
                        create = true;
                        break;
                    case "world":
                        tc.setText("" + player.getWorld().getName());
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.name);
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
                        create = true;
                        break;
                    case "prefix":
                        prefix = chatter.getTeam() != null;
                        break;
                    case "suffix":
                        tc.setText("" + chatter.getTeam().getSuffix());
                        ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + this.name);
                        he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_GRAY + "Click to focus " + this.color + this.name).create());
                        create = true;
                        break;
                    case "groupprefix":
                        break;
                    case "groupsuffix":
                        break;
                }
                if (create) {
                    tc.setColor(currentColor.asBungee());
                    tc.setClickEvent(ce);
                    tc.setHoverEvent(he);
                    root.addExtra(tc);
                }

                analyzer = "";
                inNode = false;
            } else if (c == '&') {
                nextIsColorCode = true;
            } else if (inNode) {
                analyzer += c;
            } else {
                tc = new TextComponent(String.valueOf(c));
                tc.setColor(currentColor.asBungee());
                tc.setClickEvent(ce);
                tc.setHoverEvent(he);
                root.addExtra(tc);
            }
        }
        return root;
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

    public boolean banMember(Chatter chatter, boolean bool, boolean flag) {
        return false;
    }

    public void emote(Chatter sender, String message) {
        message = this.applyFormat(this.formatSupplier.getEmoteFormat(), "").replace("%2$s", message);

        Set<Player> recipients = this.members.stream().map(Chatter::getPlayer).collect(Collectors.toSet());

        this.trimRecipients(recipients, sender);

        String finalMessage = message;
        recipients.forEach(player -> player.sendMessage(finalMessage));

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(sender, this, message));
        SurvivorBot.logChat(message);
    }

    public Set<String> getBans() {
        return new HashSet(this.bans);
    }

    public void setBans(Set<String> bans) {
        this.bans = bans;
        this.storage.flagUpdate(this);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
        this.storage.flagUpdate(this);
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance < 0 ? 0 : distance;
        this.storage.flagUpdate(this);
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
        this.storage.flagUpdate(this);
    }

    public Set<Chatter> getMembers() {
        return new HashSet(this.members);
    }

    public Set<String> getModerators() {
        return new HashSet(this.moderators);
    }

    public void setModerators(Set<String> moderators) {
        this.moderators = moderators;
        this.storage.flagUpdate(this);
    }

    public Set<String> getMutes() {
        return new HashSet(this.mutes);
    }

    public void setMutes(Set<String> mutes) {
        this.mutes = mutes;
        this.storage.flagUpdate(this);
    }

    public String getName() {
        return this.name;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
        this.storage.flagUpdate(this);
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (password == null) {
            this.password = "";
        } else {
            this.password = password;
        }

        this.storage.flagUpdate(this);
    }

    public String getDiscordChannelLinkID() {
        return this.discordChannelLinkID;
    }

    public void setDiscordChannelLinkID(String discordChannelLinkID) {
        this.discordChannelLinkID = discordChannelLinkID;
    }

    public Set<String> getWorlds() {
        return new HashSet(this.worlds);
    }

    public void setWorlds(Set<String> worlds) {
        this.worlds = worlds;
        this.storage.flagUpdate(this);
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

    public void setCrossWorld(boolean crossWorld) {
        this.crossWorld = crossWorld;
        this.storage.flagUpdate(this);
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

    public void setShortcutAllowed(boolean shortcutAllowed) {
        this.shortcutAllowed = shortcutAllowed;
        this.storage.flagUpdate(this);
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
        this.storage.flagUpdate(this);
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
        TextComponent root = this.applyFormat(event.getFormat(), event.getMessage(), player);
        Chatter sender = SurvivorBot.getChatterManager().getChatter(player);
        Set<Player> recipients = Bukkit.getOnlinePlayers().stream().collect(Collectors.toSet());

        this.trimRecipients(recipients, sender);

        String msg = root.toPlainText();

        // Send to players in channel

        recipients.forEach(player1 -> player1.spigot().sendMessage(root));

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(sender, this, msg));

        // Send to discord
        if (!discordChannelLinkID.equals("")) {
            SurvivorBot.getInstance().getDJA().getTextChannelById(discordChannelLinkID).sendMessage("<" + player.getName() + "> " + event.getMessage()).queue();
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
                    this.announce(SurvivorBot.getMessage("channel_leave").replace("$1", chatter.getPlayer().getDisplayName()));
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

    public void setModerator(String name, boolean moderator) {
        if (moderator) {
            this.moderators.add(name.toLowerCase());
        } else {
            this.moderators.remove(name.toLowerCase());
        }

        this.storage.flagUpdate(this);
    }

    public void setMuted(String name, boolean muted) {
        if (muted) {
            this.mutes.add(name.toLowerCase());
        } else {
            this.mutes.remove(name.toLowerCase());
        }

        this.storage.flagUpdate(this);
    }

    public boolean isMuted() {
        return this.muted;
    }

    public void setMuted(boolean value) {
        this.muted = value;
    }

    public void sendRawMessage(String message) {
        this.members.forEach(chatter -> chatter.getPlayer().sendMessage(message));
    }

    public MessageFormatSupplier getFormatSupplier() {
        return this.formatSupplier;
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

    public ChannelStorage getStorage() {
        return this.storage;
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.name == null ? 0 : this.name.toLowerCase().hashCode());
        result1 = 31 * result1 + (this.nick == null ? 0 : this.nick.toLowerCase().hashCode());
        return result1;
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

    public boolean kickMember(Chatter chatter, boolean bool, boolean flag) {
        return false;
    }

    private boolean isMessageHeard(Set<Player> recipients, Chatter sender) {
        if (!this.isLocal()) {
            return true;
        } else {
            Player senderPlayer = sender.getPlayer();
            int visibleRecipients = recipients.stream()
                    .filter(player -> player.hasPermission("survivorbot.admin.stealth") && !player.equals(senderPlayer))
                    .collect(Collectors.toList())
                    .size();

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
}































