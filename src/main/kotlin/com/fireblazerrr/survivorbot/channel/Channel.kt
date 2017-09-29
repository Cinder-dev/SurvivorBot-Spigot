package com.fireblazerrr.survivorbot.channel

import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.spigot.events.ChannelChatEvent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.World
import org.bukkit.entity.Player

interface Channel {

    val name: String
    var nick: String
    var format: String
    var password: String
    var discordChannelLinkID: String
    var color: ChatColor
    var distance: Int
    var shortcutAllowed: Boolean
    var verbose: Boolean
    var crossWorld: Boolean
    var muted: Boolean
    val members: MutableSet<Chatter>

    fun addMember(chatter: Chatter, announce: Boolean, flagUpdate: Boolean): Boolean
    fun addWorld(world: String)
    fun announce(message: String)
    fun announce(message: TextComponent)
    fun applyFormat(format: String, message: String, player: String): TextComponent
    fun applyFormat(format: String, message: String, player: Player): TextComponent
    fun applyFormat(format: String, originalFormat: String): String
    fun banMember(chatter: Chatter, announce: Boolean, flag: Boolean): Boolean
    fun emote(chatter: Chatter, message: String)
    fun setBans(chatter: Set<String>)
    fun setModerators(chatters: Set<String>)
    fun setMutes(chatters: Set<String>)
    fun hasWorld(world: World): Boolean
    fun hasWorld(world: String): Boolean
    fun isHidden(): Boolean
    fun isLocal(): Boolean
    fun isChatterMember(name: String): Boolean
    fun isChatterBanned(name: String): Boolean
    fun isChatterModerator(name: String): Boolean
    fun isChatterMuted(name: String): Boolean
    fun isTransient(): Boolean
    fun kickMember(chatter: Chatter, announce: Boolean): Boolean
    fun onFocusGain(chatter: Chatter)
    fun onFocusLoss(chatter: Chatter)
    fun processChat(event: ChannelChatEvent)
    fun removeMember(chatter: Chatter, announce: Boolean, flag: Boolean)
    fun removeWorld(world: String)
    fun setChatterBanned(chatter: String, banned: Boolean)
    fun setChatterModerator(chatter: String, moderator: Boolean)
    fun setChatterMuted(chatter: String, muted: Boolean)
    fun sendRawMessage(message: String)
}