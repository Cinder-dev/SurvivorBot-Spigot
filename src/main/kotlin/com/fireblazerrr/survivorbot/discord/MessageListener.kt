package com.fireblazerrr.survivorbot.discord

import com.fireblazerrr.survivorbot.SurvivorBot
import com.fireblazerrr.survivorbot.channel.Channel
import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.utils.Announcement
import com.fireblazerrr.survivorbot.utils.ColorUtils
import com.google.gson.JsonObject
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.awt.Color

class MessageListener : ListenerAdapter() {

    private val cu: ColorUtils = ColorUtils()

    companion object {
        fun format(channel: Channel, sender: String, message: String): String {
            val form: String = channel.applyFormat(SurvivorBot.channelManager.discordFormat, "")
                    .replace("{prefix}", "")
                    .replace("{suffix}", "")
                    .replace("1$", "")
                    .replace("2$", "")
            return String.format(form, sender, message)
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // Check if message is new announcement
        if (event.channel.id == SurvivorBot.discordBot.announcementChannelID) {
            Announcement(Bukkit.getOnlinePlayers().toList())
            return
        }

        if (!event.author.isBot) {
            val channelID: String = event.message.textChannel.id
            val sender: String = "${getDiscordColor(event.member.color)}${event.message.author.name}"
            val message: String = event.message.rawContent
            val status: String = if (SurvivorBot.discordBot.jda.getGuildById(event.guild.id).getMember(event.message.author).game.name == null)
                ""
            else
                "Playing: ${SurvivorBot.discordBot.jda.getGuildById(event.guild.id).getMember(event.message.author).game.name}"

            SurvivorBot.channelManager.getChannels()
                    .filter { it.discordChannelLinkID == channelID }
                    .forEach { channel: Channel ->
                        channel.members
                                .map(Chatter::player)
                                .map(Player::spigot)
                                .forEach {
                                    val colorized: String = ChatColor.translateAlternateColorCodes('&', format(channel, sender, message))
                                    val root = TextComponent(TextComponent.fromLegacyText(colorized).first())
                                    root.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, SurvivorBot.discordBot.inviteURL)
                                    root.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            ComponentBuilder("${ChatColor.BOLD}$sender${ChatColor.RESET}\n$status"
                                            ).create())
                                    it.sendMessage(root)
                                }
                    }
            val jsonObject = JsonObject()
            with(jsonObject) {
                addProperty("id", "discord")
                addProperty("channel", channelID)
                addProperty("color", getDiscordColor(event.member.color).getName())
                addProperty("user", ChatColor.stripColor(sender))
                addProperty("userStatus", status)
                addProperty("message", message)
                addProperty("inviteURL", SurvivorBot.discordBot.inviteURL)
                SurvivorBot.publish("survivor", jsonObject.toString())
            }
        }
    }

    private fun getDiscordColor(color: Color): ChatColor = cu.getColorNameFromColor(color)


}