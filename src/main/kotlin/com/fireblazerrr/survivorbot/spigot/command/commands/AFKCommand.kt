package com.fireblazerrr.survivorbot.spigot.command.commands

import com.fireblazerrr.survivorbot.SurvivorBot
import com.fireblazerrr.survivorbot.chatter.Chatter
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AFKCommand : BasicCommand("AFK"){
    override val description: String = SurvivorBot.getMessage("command_afk")
    override val usage: String = "/afk ${ChatColor.DARK_GRAY} [message]"
    override val minArguments: Int = 0
    override val maxArguments: Int = Int.MAX_VALUE
    override val identifiers: Array<String> = arrayOf("afk", "ch afk", "survivorbot afk")
    override val notes: Array<String> = arrayOf("Toggle afk notifications")

    override fun execute(sender: CommandSender, identifier: String, args: Array<String>): Boolean {
        val player: Player = sender as Player
        val chatter: Chatter = SurvivorBot.chatterManager.getChatter(player)



        return true
    }
}