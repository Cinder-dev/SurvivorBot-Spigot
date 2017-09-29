package com.fireblazerrr.survivorbot.spigot.command

import org.bukkit.command.CommandSender

interface Command{
    fun execute(sender: CommandSender, identifier: String, args: Array<String>): Boolean
    fun isIdentifier(key: String): Boolean
    fun cancelInteraction(commandSender: CommandSender)
    fun isInProgress(commandSender: CommandSender): Boolean
    fun isInteractive(): Boolean
    fun isShownOnHelpMenu(): Boolean

}