package com.fireblazerrr.survivorbot.channel

import com.fireblazerrr.survivorbot.SurvivorBot
import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.utils.createIfMissing
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.PluginManager
import java.util.*
import kotlin.collections.ArrayList

class ChannelManager {

    var defaultChannel: Channel
    var standardFormat: String = "{color}[{nick}{color}] &f{prefix}{sender}{suffix}{color}: {msg}"
    var emoteFormat: String = "{color}[{nick}{color}] * {msg}"
    var announceFormat: String = "{msg}"
    var discordFormat: String = "&9<Discord:{sender}&9> {msg}"
    var conversationFormat: String = "&d{convoaddress} {convopartner}: {msg}"
    var usingEmotes: Boolean = true

    private val storage: ChannelStorage = YMLChannelStorage(createIfMissing(SurvivorBot.getDataLocation(), "channels"))
    private val channels: MutableMap<String, Channel> = HashMap()
    private val wildcardPermissions: MutableMap<Chatter.Permission, Permission> = EnumMap(Chatter.Permission::class.java)
    private val modPermissions: MutableSet<Chatter.Permission> = EnumSet.noneOf(Chatter.Permission::class.java)

    init {
        registerChannelPermissions()
        defaultChannel = channels.values.first()
    }

    fun addChannel(channel: Channel) {
        channels.put(channel.name.toLowerCase(), channel)
        channels.put(channel.nick.toLowerCase(), channel)
        if (channel.discordChannelLinkID.isNotBlank())
            channels.put(channel.discordChannelLinkID, channel)
        if (channel.isTransient()) {
            val permissions: Array<Chatter.Permission> = Chatter.Permission.values()
            permissions.forEach { forceJoinPermission ->
                val forceLeavePermission: Permission = wildcardPermissions.getValue(forceJoinPermission)

                forceLeavePermission.children.put(forceJoinPermission.form(channel).toLowerCase(), true)
                forceLeavePermission.recalculatePermissibles()
            }

            val pluginManager: PluginManager = Bukkit.getServer().pluginManager
            val focus: String = Chatter.Permission.FOCUS.form(channel).toLowerCase()
            pluginManager.addPermission(Permission(focus, PermissionDefault.TRUE))

            val autoJoin: String = Chatter.Permission.AUTOJOIN.form(channel).toLowerCase()
            pluginManager.addPermission(Permission(autoJoin, PermissionDefault.FALSE))

            val forceJoin: String = Chatter.Permission.FORCE_JOIN.form(channel).toLowerCase()
            pluginManager.addPermission(Permission(forceJoin, PermissionDefault.FALSE))

            val forceLeave: String = Chatter.Permission.FORCE_LEAVE.form(channel).toLowerCase()
            pluginManager.addPermission(Permission(forceLeave, PermissionDefault.FALSE))

            storage.addChannel(channel)
        }
    }

    fun addModPermission(permission: Chatter.Permission) {
        modPermissions.add(permission)
    }

    fun checkModPermission(permission: Chatter.Permission): Boolean = modPermissions.contains(permission)

    fun clear() {
        channels.clear()
        modPermissions.clear()
        standardFormat = "{color}[{nick}{color}] &f{prefix}{sender}{suffix}{color}: {msg}"
        emoteFormat = "{color}[{nick}{color}] * {msg}"
        announceFormat = "{msg}"
        discordFormat = "&9<Discord:{sender}&9> {msg}"
        conversationFormat = "&d{convoaddress} {convopartner}: {msg}"
    }

    fun getChannel(identifier: String): Channel = channels.getValue(identifier.toLowerCase())

    fun getChannels(): List<Channel> {
        val list: ArrayList<Channel> = ArrayList()
        channels.values.forEach {
            if (!list.contains(it))
                list.add(it)
        }
        return list
    }

    fun hasChannel(identifier: String): Boolean = channels.containsKey(identifier.toLowerCase())

    fun loadChannels(){
        storage.loadChannels().forEach(this::addChannel)
    }

    private fun registerChannelPermissions(){
        val pluginManager: PluginManager = Bukkit.getServer().pluginManager
        Chatter.Permission.values().forEach {
            val perm = Permission(it.formWildcard(), PermissionDefault.FALSE)
            pluginManager.addPermission(perm)
            wildcardPermissions.put(it, perm)
        }
    }

    fun removeChannel(channel: Channel){
        channels.remove(channel.name.toLowerCase())
        channels.remove(channel.nick.toLowerCase())
        if (!channel.isTransient()){
            Chatter.Permission.values().forEach {
                val forceLeavePermission = wildcardPermissions.getValue(it)
                forceLeavePermission.children.remove(it.form(channel).toLowerCase())
                forceLeavePermission.recalculatePermissibles()
            }

            val pluginManager: PluginManager = Bukkit.getServer().pluginManager
            val focus: String = Chatter.Permission.FOCUS.form(channel).toLowerCase()
            pluginManager.removePermission(focus)

            val autoJoin: String = Chatter.Permission.AUTOJOIN.form(channel).toLowerCase()
            pluginManager.removePermission(autoJoin)

            val forceJoin: String = Chatter.Permission.FORCE_JOIN.form(channel).toLowerCase()
            pluginManager.removePermission(forceJoin)

            val forceLeave: String = Chatter.Permission.FORCE_LEAVE.form(channel).toLowerCase()
            pluginManager.removePermission(forceLeave)

            storage.removeChannel(channel)
        }
    }
}