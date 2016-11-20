package com.fireblazerrr.survivorbot.channel;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChannelChatEvent;
import com.fireblazerrr.survivorbot.spigot.events.custom.ChatCompleteEvent;
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ConversationChannel extends StandardChannel {
    public ConversationChannel(Chatter memberOne, Chatter memberTwo, MessageFormatSupplier formatSupplier) {
        super(SurvivorBot.getChannelManager().getStorage(), "convo" + memberOne.getName() + memberTwo.getName(), "convo" + memberTwo.getName() + memberOne.getName(), formatSupplier);
        super.addMember(memberOne, false, false);
        super.addMember(memberTwo, false, false);
        this.setFormat(formatSupplier.getConversationFormat());
    }

    public boolean addMember(Chatter chatter, boolean announce, boolean flagUpdate) {
        return this.getMembers().size() < 2 && super.addMember(chatter, false, false);
    }

    public void addWorld(String world) {
    }

    public String applyFormat(String format, Player sender, Player recipient) {
        Player target = null;
        if (sender.equals(recipient)) {
            target = this.getMembers().stream().filter(chatter -> !chatter.getPlayer().equals(sender)).map(Chatter::getPlayer).findFirst().get();

            if (target != null) {
                format = format.replace("{convoaddress}", "To").replace("{convopartner}", target.getDisplayName());
            }
        } else {
            format = format.replace("{convoaddress}", "From").replace("{convopartner}", sender.getDisplayName());
        }

        format = format.replaceAll("(?i)&([0-9a-fk-or])", "§$1");
        return format;
    }

    public boolean banMember(Chatter chatter, boolean announce) {
        return false;
    }

    public Set<String> getBans() {
        return new HashSet();
    }

    public int getDistance() {
        return 0;
    }

    public Set<String> getModerators() {
        return new HashSet();
    }

    public Set<String> getMutes() {
        return new HashSet();
    }

    public String getPassword() {
        return "";
    }

    public Set<String> getWorlds() {
        return new HashSet();
    }

    public boolean hasWorld(World world) {
        return true;
    }

    public boolean isBanned(String name) {
        return false;
    }

    public boolean isHidden() {
        return true;
    }

    public boolean isLocal() {
        return false;
    }

    public boolean isModerator(String name) {
        return false;
    }

    public boolean isMuted(String name) {
        return false;
    }

    public boolean isShortcutAllowed() {
        return false;
    }

    public boolean isTransient() {
        return true;
    }

    public boolean kickMember(Chatter chatter, boolean announce) {
        return false;
    }

    public void onFocusLoss(Chatter chatter) {
        this.getMembers().stream().filter(chatter1 -> chatter1.getActiveChannel() != null && chatter1.getActiveChannel().equals(this)).forEach(chatter1 -> {
            return;
        });

        this.getMembers().forEach(chatter1 -> chatter1.removeChannel(this, false, true));

        SurvivorBot.getChannelManager().removeChannel(this);
    }

    public void processChat(ChannelChatEvent event) {
        Player player = event.getSender().getPlayer();
        String senderName = player.getName();
        Chatter sender = SurvivorBot.getChatterManager().getChatter(player);
        String format = event.getFormat();

        this.getMembers()
                .stream()
                .filter(chatter -> !(chatter.isIgnoring(senderName)) && !(chatter.canIgnore(SurvivorBot.getChatterManager().getChatter(senderName)) != Chatter.Result.NO_PERMISSION))
                .forEach(chatter -> {
                    String afkMsg = this.applyFormat(format, player, chatter.getPlayer());
                    chatter.getPlayer().sendMessage(afkMsg.replace("{msg}", event.getMessage()));
                    if (!sender.equals(chatter) && chatter.isAFK()) {
                        afkMsg = chatter.getAFKMessage();

                        try {
                            afkMsg = afkMsg.isEmpty() ? "<AFK> " + SurvivorBot.getMessage("convo_afk") : "AFK " + afkMsg;
                        } catch (MessageNotFoundException e) {
                            SurvivorBot.severe("Messages.properties is missing: convo_afk");
                        }

                        player.sendMessage(this.applyFormat(format, chatter.getPlayer(), player).replace("{msg}", afkMsg));
                    }

                    if (!sender.equals(chatter)) {
                        chatter.setLastPrivateMessageSource(sender);
                        SurvivorBot.logChat(senderName + " -> " + chatter.getName() + ": " + event.getMessage());
                    }
                });

        Bukkit.getPluginManager().callEvent(new ChatCompleteEvent(sender, this, event.getMessage()));
    }

    public boolean removeMember(Chatter chatter, boolean announce, boolean flagUpdate) {
        if (super.removeMember(chatter, false, flagUpdate)) {
            int count = this.getMembers().size();
            if (count == 1) {
                Chatter otherMember = this.getMembers().iterator().next();
                this.removeMember(otherMember, false, flagUpdate);
                if (otherMember.getActiveChannel() != null && otherMember.getActiveChannel().equals(this)) {
                    otherMember.setActiveChannel(null, true, flagUpdate);
                }

                SurvivorBot.getChannelManager().removeChannel(this);
            }

            return true;
        } else {
            return false;
        }
    }

    public void removeWorld(String world) {
    }

    public void setBanned(String name, boolean banned) {
    }

    public void setBans(Set<String> bans) {
    }

    public void setModerator(String name, boolean moderator) {
    }

    public void setModerators(Set<String> moderators) {
    }

    public void setMuted(String name, boolean muted) {
    }

    public void setMutes(Set<String> mutes) {
    }

    public void setNick(String nick) {
    }

    public void setPassword(String password) {
    }

    public void setShortcutAllowed(boolean shortcutAllowed) {
    }

    public void setWorlds(Set<String> worlds) {
    }
}












