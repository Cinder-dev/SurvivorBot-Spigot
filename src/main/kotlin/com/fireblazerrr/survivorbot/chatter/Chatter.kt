package com.fireblazerrr.survivorbot.chatter

import com.fireblazerrr.survivorbot.channel.Channel
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

interface Chatter {

    val player: Player
    var lastPMSource: Chatter
    var activeChannel: Channel
    var lastActiveChannel: Channel
    var lastFocusableChannel: Channel
    var afkMessage: String
    var muted: Boolean
    var afk: Boolean
    var team: Team?

    fun addChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean): Boolean
    fun canBan(channel: Channel): Result
    fun canColorMessage(channel: Channel, color: ChatColor): Result
    fun canEmote(channel: Channel): Result
    fun canFocus(channel: Channel): Result
    fun canJoin(channel: Channel, password: String): Result
    fun canKick(channel: Channel): Result
    fun canLeave(channel: Channel): Result
    fun canModify(setting: String, channel: Channel): Result
    fun canMute(channel: Channel): Result
    fun canRemove(channel: Channel): Result
    fun canSpeak(channel: Channel): Result
    fun canViewInfo(channel: Channel): Result
    fun canIgnore(other: Chatter): Result
    fun isIgnoring(chatter: Chatter): Boolean
    fun isIgnoring(chatter: String): Boolean
    fun isInRange(chatter: Chatter, range: Int): Boolean
    fun removeChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean): Boolean
    fun setActiveChannel(channel: Channel, announce: Boolean, flagUpdate: Boolean)
    fun setIgnore(name: String, ignore: Boolean, flagUpdate: Boolean)
    fun shouldAutoJoin(channel: Channel): Boolean
    fun shouldForceJoin(channel: Channel): Boolean
    fun shouldForceLeave(channel: Channel): Boolean
    fun hasChannel(channel: Channel): Boolean
    fun refocus()
    fun disconnect()


    enum class Result {
        NO_PERMISSION,
        NO_CHANNEL,
        INVALID,
        BANNED,
        MUTED,
        ALLOWED,
        BAD_WORLD,
        BAD_PASSWORD,
        FAIL
    }

    enum class Permission(private val permission: String) {
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

        fun form(channel: Channel): String = "survivorbot.$permission.${channel.name}"
        fun formAll(): String = "survivorbot.$permission.all"
        fun formWildcard(): String = "survivorbot.$permission.*"
        override fun toString(): String = permission
    }
}