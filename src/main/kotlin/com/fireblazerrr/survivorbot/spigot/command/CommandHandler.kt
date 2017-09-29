package com.fireblazerrr.survivorbot.spigot.command

import com.fireblazerrr.survivorbot.utils.message.Messaging
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object CommandHandler {

    val commands: MutableMap<String, Command> = LinkedHashMap()
    private val identifiers: MutableMap<String, Command> = HashMap()

    fun hasPermission(sender: CommandSender, permission: String): Boolean {
        if (sender.isOp) return true
        else if (sender is Player && permission.isNotEmpty())
            return sender.hasPermission(permission)
        return true
    }

    fun addCommand(command: Command) {
        commands.put(command.name.toLowerCase(), command)
        command.identifiers.forEach { identifier: String -> identifiers.put(identifier.toLowerCase(), command) }
    }

    fun dispatch(sender: CommandSender, label: String, args: Array<String>): Boolean {
        args.size.downTo(0).forEach { argsIncluded ->
            val identifier: StringBuilder = StringBuilder(label)

            0.until(argsIncluded).forEach { cmd: Int -> identifier.append(" ${args[cmd]}") }

            val command: Command? = this.identifiers[identifier.toString().toLowerCase()]
            if (command != null) {
                val realArgs: Array<String> = args.copyOfRange(argsIncluded, args.size)
                if (realArgs.size < command.minArguments || realArgs.size > command.maxArguments) {
                    displayCommandHelp(command, sender)
                    return true
                }
                if (realArgs.isNotEmpty() && realArgs[0] == "?") {
                    displayCommandHelp(command, sender)
                    return true
                }

                if (command.permission.isNotEmpty() && !sender.hasPermission(command.permission)) {
                    Messaging.send(sender, "Insufficient permission.")
                    return true
                }

                command.execute(sender, identifier.toString(), realArgs);
                return true
            }
        }
        return true
    }

    private fun displayCommandHelp(cmd: Command, sender: CommandSender) {
        sender.sendMessage(arrayOf(
                "${ChatColor.RED}-----${ChatColor.WHITE}[ Command Help ]${ChatColor.RED}-----",
                "${ChatColor.RED}Command:${ChatColor.YELLOW} ${cmd.name}",
                "${ChatColor.RED}Description:${ChatColor.YELLOW} ${cmd.description}",
                "${ChatColor.RED}Usage:${ChatColor.YELLOW} ${cmd.usage}"
        ))
        cmd.notes.forEach { note: String -> sender.sendMessage("${ChatColor.YELLOW} $note") }
    }
}