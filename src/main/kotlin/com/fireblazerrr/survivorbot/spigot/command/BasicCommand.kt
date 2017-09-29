package com.fireblazerrr.survivorbot.spigot.command

import org.bukkit.command.CommandSender

abstract class BasicCommand(val name: String) : Command {
    abstract val description: String
    abstract val usage: String
    abstract val minArguments: Int
    abstract val maxArguments: Int
    abstract val identifiers: Array<String>
    abstract val notes: Array<String>
    val permission: String = ""

    override fun cancelInteraction(commandSender: CommandSender) {}
    override fun isIdentifier(key: String): Boolean = this.identifiers.any { identifier: String -> key.equals(identifier, true) }
    override fun isInProgress(commandSender: CommandSender): Boolean = false
    override fun isShownOnHelpMenu(): Boolean = true
    override fun isInteractive(): Boolean = false
}