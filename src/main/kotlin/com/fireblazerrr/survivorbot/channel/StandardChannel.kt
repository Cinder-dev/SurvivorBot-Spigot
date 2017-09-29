package com.fireblazerrr.survivorbot.channel

import com.fireblazerrr.survivorbot.SurvivorBot
import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.spigot.events.ChannelChatEvent
import com.fireblazerrr.survivorbot.utils.message.MessageFormatSupplier
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.regex.Pattern

class StandardChannel(private val storage: ChannelStorage, override val name: String, override var nick: String, private val formatSupplier: MessageFormatSupplier): Channel{

    private val msgPattern: Pattern = Pattern.compile("(.*)<(.*)%1\\\$s(.*)> $2\\\$s")
    override var format: String = "{default}"
    override var password: String = ""
    override var discordChannelLinkID: String = ""
    override var color: ChatColor = ChatColor.WHITE
    override var distance: Int = 0
    override var shortcutAllowed: Boolean = false
    override var verbose: Boolean = true
    override var crossWorld: Boolean = true
    override var muted: Boolean = false
    private val members: MutableSet<Chatter> = HashSet()
    private val worlds: Set<String> = emptySet()
    private val bans: Set<String> = emptySet()
    private val moderators: Set<String> = emptySet()
    private val mutes: Set<String> = emptySet()

    override fun addMember(chatter: Chatter, announce: Boolean, flagUpdate: Boolean): Boolean {
        if (members.add(chatter)) {
            if (announce && verbose)
                announce(SurvivorBot.getMessage("channel_join").replace("$1", chatter.player.displayName))

            chatter.addChannel(this, announce, flagUpdate)

            return true
        }
        return false
    }

    override fun addWorld(world: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun announce(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun announce(message: TextComponent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyFormat(format: String, message: String, player: String): TextComponent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyFormat(format: String, message: String, player: Player): TextComponent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyFormat(format: String, originalFormat: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun banMember(chatter: Chatter, announce: Boolean, flag: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emote(chatter: Chatter, message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBans(chatter: Set<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setModerators(chatters: Set<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMutes(chatters: Set<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasWorld(world: World): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasWorld(world: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isHidden(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLocal(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChatterMember(name: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChatterBanned(name: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChatterModerator(name: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isChatterMuted(name: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isTransient(): Boolean = false

    override fun kickMember(chatter: Chatter, announce: Boolean): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFocusGain(chatter: Chatter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFocusLoss(chatter: Chatter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processChat(event: ChannelChatEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMember(chatter: Chatter, announce: Boolean, flag: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeWorld(world: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChatterBanned(chatter: String, banned: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChatterModerator(chatter: String, moderator: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setChatterMuted(chatter: String, muted: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendRawMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}