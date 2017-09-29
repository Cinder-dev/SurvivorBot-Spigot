package com.fireblazerrr.survivorbot.chatter

import com.fireblazerrr.survivorbot.SurvivorBot
import com.fireblazerrr.survivorbot.channel.Channel
import com.fireblazerrr.survivorbot.utils.message.Messaging
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team


class StandardChatter(private val storage: ChatterStorage, override val player: Player) : Chatter {
    override var lastPMSource: Chatter = this
    override var activeChannel: Channel = SurvivorBot.channelManager.defaultChannel
    override var lastActiveChannel: Channel = SurvivorBot.channelManager.defaultChannel
    override var lastFocusableChannel: Channel = SurvivorBot.channelManager.defaultChannel
    override var afkMessage: String = ""
    override var muted: Boolean = false
    override var afk: Boolean = false
    override var team: Team? = Bukkit.getScoreboardManager().mainScoreboard.teams.firstOrNull { it.entries.contains(player.name) }

    private val channels: MutableSet<Channel> = HashSet()
    private val ignores: MutableSet<String> = HashSet()

    override fun addChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean): Boolean {
        if (channels.add(channel)) {
            channel.addMember(this, announce, flagUpdate)

            if (flagUpdate) storage.flagUpdate(this)

            return true
        }
        return false
    }


    override fun canBan(channel: Channel): Chatter.Result = if (SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.BAN))
        Chatter.Result.ALLOWED
    else
        if (channel.isChatterModerator(player.name) && SurvivorBot.channelManager.checkModPermission(Chatter.Permission.BAN))
            Chatter.Result.ALLOWED
        else
            Chatter.Result.NO_PERMISSION


    override fun canColorMessage(channel: Channel, color: ChatColor): Chatter.Result = Chatter.Result.ALLOWED


    override fun canEmote(channel: Channel): Chatter.Result = if (!channel.isChatterMember(this.player.name))
        Chatter.Result.INVALID
    else
        if (!channel.isTransient() && SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.EMOTE))
            if (!muted && !channel.isChatterMuted(player.name))
                if (!channel.hasWorld(player.world))
                    Chatter.Result.BAD_WORLD
                else
                    Chatter.Result.ALLOWED
            else
                Chatter.Result.MUTED
        else
            Chatter.Result.NO_PERMISSION


    override fun canFocus(channel: Channel): Chatter.Result = if (!SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.FOCUS))
        Chatter.Result.NO_PERMISSION
    else {
        val speak: Chatter.Result = canSpeak(channel)

        if (speak != Chatter.Result.ALLOWED && speak != Chatter.Result.INVALID)
            Chatter.Result.NO_PERMISSION
        else
            Chatter.Result.ALLOWED
    }


    override fun canJoin(channel: Channel, password: String): Chatter.Result = if (channel.isChatterMember(player.name))
        Chatter.Result.INVALID
    else
        if (!SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.JOIN))
            Chatter.Result.NO_PERMISSION
        else
            if (channel.isChatterBanned(player.name))
                Chatter.Result.BANNED
            else
                if (password != channel.password)
                    Chatter.Result.BAD_PASSWORD
                else
                    Chatter.Result.ALLOWED


    override fun canKick(channel: Channel): Chatter.Result = if (SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.KICK))
        Chatter.Result.ALLOWED
    else
        if (channel.isChatterModerator(player.name) && SurvivorBot.channelManager.checkModPermission(Chatter.Permission.KICK))
            Chatter.Result.ALLOWED
        else
            Chatter.Result.NO_PERMISSION


    override fun canLeave(channel: Channel): Chatter.Result = if (!channel.isChatterMember(player.name))
        Chatter.Result.INVALID
    else
        if (SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.LEAVE))
            Chatter.Result.NO_PERMISSION
        else
            Chatter.Result.ALLOWED


    override fun canModify(setting: String, channel: Channel): Chatter.Result {
        val lowerSetting = setting.toLowerCase()
        val permission: Chatter.Permission = when (lowerSetting) {
            "nick" -> Chatter.Permission.MODIFY_NICK
            "format" -> Chatter.Permission.MODIFY_FORMAT
            "distance" -> Chatter.Permission.MODIFY_DISTANCE
            "color" -> Chatter.Permission.MODIFY_COLOR
            "shortcut" -> Chatter.Permission.MODIFY_SHORTCUT
            "password" -> Chatter.Permission.MODIFY_PASSWORD
            "verbose" -> Chatter.Permission.MODIFY_VERBOSE
            "chatcost" -> Chatter.Permission.MODIFY_CHATCOST
            "crossworld" -> Chatter.Permission.MODIFY_CROSSWORLD
            else -> {
                if (setting != "focusable")
                    return Chatter.Result.INVALID

                Chatter.Permission.MODIFY_FOCUSABLE
            }
        }

        return if (SurvivorBot.hasChannelPermission(player, channel, permission))
            Chatter.Result.ALLOWED
        else
            if (channel.isChatterModerator(player.name) && SurvivorBot.channelManager.checkModPermission(permission))
                Chatter.Result.ALLOWED
            else
                Chatter.Result.NO_PERMISSION
    }


    override fun canMute(channel: Channel): Chatter.Result = if (SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.MUTE))
        Chatter.Result.ALLOWED
    else
        if (channel.isChatterModerator(player.name) && SurvivorBot.channelManager.checkModPermission(Chatter.Permission.BAN))
            Chatter.Result.ALLOWED
        else
            Chatter.Result.NO_PERMISSION


    override fun canRemove(channel: Channel): Chatter.Result = if (SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.REMOVE))
        Chatter.Result.ALLOWED
    else
        if (channel.isChatterModerator(player.name) && SurvivorBot.channelManager.checkModPermission(Chatter.Permission.REMOVE))
            Chatter.Result.ALLOWED
        else
            Chatter.Result.NO_PERMISSION


    override fun canSpeak(channel: Channel): Chatter.Result = if (!channel.isChatterMember(player.name))
        Chatter.Result.INVALID
    else
        if (!channel.isTransient() && SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.SPEAK))
            Chatter.Result.NO_PERMISSION
        else
            if (!muted && !channel.isChatterMuted(player.name))
                if (!channel.hasWorld(player.world))
                    Chatter.Result.BAD_WORLD
                else
                    Chatter.Result.ALLOWED
            else
                Chatter.Result.MUTED


    override fun canViewInfo(channel: Channel): Chatter.Result = if (!SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.INFO))
        Chatter.Result.NO_PERMISSION
    else
        Chatter.Result.ALLOWED


    override fun canIgnore(other: Chatter): Chatter.Result = if (other.player.hasPermission("survivorbot.admin.unignore"))
        Chatter.Result.NO_PERMISSION
    else
        Chatter.Result.ALLOWED


    override fun isIgnoring(chatter: Chatter): Boolean = isIgnoring(chatter.player.name)
    override fun isIgnoring(chatter: String): Boolean = ignores.contains(chatter.toLowerCase())

    override fun isInRange(chatter: Chatter, range: Int): Boolean = (player.world == chatter.player.world && player.location.distanceSquared(chatter.player.location) <= (range * range))

    override fun removeChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean): Boolean {
        if (channels.contains(channel)) {
            channels.remove(channel)
            if (channel.isChatterMember(player.name))
                channel.removeMember(this, announce, flagUpdate)

            if (flagUpdate)
                this.storage.flagUpdate(this)

            return true
        }
        return false
    }

    override fun setActiveChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean) {
        if (channel != activeChannel) {
            activeChannel.onFocusLoss(this)

            if (!activeChannel.isTransient()) {
                lastActiveChannel = activeChannel
                if (canFocus(activeChannel) == (Chatter.Result.ALLOWED)) {
                    lastFocusableChannel = activeChannel
                }
            }

            activeChannel = channel
            activeChannel.onFocusGain(this)
            if (announce)
                Messaging.send(player, SurvivorBot.getMessage("chatter_focus"), "${channel.color}${channel.name}")
            if (flagUpdate)
                storage.flagUpdate(this)

        }
    }

    override fun setIgnore(name: String, ignore: Boolean, flagUpdate: Boolean) {
        if (ignore)
            ignores.add(name.toLowerCase())
        else
            ignores.remove(name.toLowerCase())


        if (flagUpdate)
            storage.flagUpdate(this)
    }

    override fun shouldAutoJoin(channel: Channel): Boolean = SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.AUTOJOIN)

    override fun shouldForceJoin(channel: Channel): Boolean = SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.FORCE_JOIN)

    override fun shouldForceLeave(channel: Channel): Boolean = SurvivorBot.hasChannelPermission(player, channel, Chatter.Permission.FORCE_LEAVE)

    override fun hasChannel(channel: Channel): Boolean = channels.contains(channel)

    override fun refocus() {
        if (canFocus(activeChannel) == Chatter.Result.ALLOWED)
            return
        lastActiveChannel = activeChannel
        lastActiveChannel.onFocusLoss(this)

        channels.filter { canFocus(it) == Chatter.Result.ALLOWED }.forEach{
            activeChannel = it
            activeChannel.onFocusGain(this)

            Messaging.send(player, SurvivorBot.getMessage("chatter_focus"), "${it.color}${it.name}")
        }
    }

    override fun disconnect() {
        val removal: Iterator<Channel> = channels.iterator()
        while (removal.hasNext()){
            val ch: Channel = removal.next()
            ch.removeMember(this, false, false)
        }
    }
}
