package com.fireblazerrr.survivorbot

import com.fireblazerrr.survivorbot.channel.Channel
import com.fireblazerrr.survivorbot.channel.ChannelManager
import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.chatter.ChatterManager
import com.fireblazerrr.survivorbot.discord.DiscordBot
import com.fireblazerrr.survivorbot.jedis.JedisListener
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.Protocol
import java.util.*
import java.util.logging.Logger

class SurvivorBot : JavaPlugin() {
    private val log: Logger = Logger.getLogger("Minecraft")
    private val chatLog: Logger = Logger.getLogger("SurvivorBot")

    companion object {
        val chatterManager: ChatterManager = ChatterManager()
        val channelManager: ChannelManager = ChannelManager()
        val discordBot: DiscordBot = DiscordBot()
        lateinit var jedisPool: JedisPool
        val jedisListener: JedisListener = JedisListener()
        var jedisHost: String = ""
        var jedisPort: Int = 0
        var jedisPassword: String = ""


        private val localization: ResourceBundle = ResourceBundle.getBundle("messages", Locale.US)

        fun getMessage(key: String): String {
            return localization.getString(key)
        }

        fun getDataLocation(): String {
            return getPlugin(SurvivorBot::class.java).dataFolder.absolutePath
        }

        fun hasChannelPermission(player: Player, channel: Channel, permission: Chatter.Permission): Boolean {
            val formedPermission: String = permission.form(channel).toLowerCase()
            return if (player.isPermissionSet(formedPermission))
                player.hasPermission(formedPermission)
            else
                player.hasPermission(permission.formAll())
        }

        fun publish(channel: String, message: String) {
            val jedis: Jedis = jedisPool.resource
            jedis.publish(channel, message)
            jedis.close()
        }
    }

    override fun onEnable() {
        SurvivorBot.discordBot.start()

        val config = JedisPoolConfig()
        config.blockWhenExhausted = false
        SurvivorBot.jedisPool = JedisPool(
                config,
                jedisHost,
                jedisPort,
                Protocol.DEFAULT_TIMEOUT,
                if (jedisPassword == "") null else jedisPassword,
                Protocol.DEFAULT_DATABASE,
                null
        )
        kotlin.run { jedisPool.resource.subscribe(jedisListener, "survivor") }
    }
}