package com.fireblazerrr.survivorbot.utils.message

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object Messaging {

    private val colors: Map<String, ChatColor> = mapOf(
            "black" to ChatColor.BLACK,
            "0" to ChatColor.BLACK,
            "dark blue" to ChatColor.DARK_BLUE,
            "dark_blue" to ChatColor.DARK_BLUE,
            "1" to ChatColor.DARK_BLUE,
            "dark green" to ChatColor.DARK_GREEN,
            "dark_green" to ChatColor.DARK_GREEN,
            "2" to ChatColor.DARK_GREEN,
            "dark aqua" to ChatColor.DARK_AQUA,
            "dark_aqua" to ChatColor.DARK_AQUA,
            "teal" to ChatColor.DARK_AQUA,
            "3" to ChatColor.DARK_AQUA,
            "dark red" to ChatColor.DARK_RED,
            "dark_red" to ChatColor.DARK_RED,
            "4" to ChatColor.DARK_RED,
            "dark purple" to ChatColor.DARK_PURPLE,
            "dark_purple" to ChatColor.DARK_PURPLE,
            "purple" to ChatColor.DARK_PURPLE,
            "5" to ChatColor.DARK_PURPLE,
            "gold" to ChatColor.GOLD,
            "orange" to ChatColor.GOLD,
            "6" to ChatColor.GOLD,
            "gray" to ChatColor.GRAY,
            "grey" to ChatColor.GRAY,
            "7" to ChatColor.GRAY,
            "dark gray" to ChatColor.DARK_GRAY,
            "dark_gray" to ChatColor.DARK_GRAY,
            "dark grey" to ChatColor.DARK_GRAY,
            "dark_grey" to ChatColor.DARK_GRAY,
            "8" to ChatColor.DARK_GRAY,
            "blue" to ChatColor.BLUE,
            "9" to ChatColor.BLUE,
            "bright green" to ChatColor.GREEN,
            "bright_green" to ChatColor.GREEN,
            "green" to ChatColor.GREEN,
            "a" to ChatColor.GREEN,
            "aqua" to ChatColor.AQUA,
            "b" to ChatColor.AQUA,
            "red" to ChatColor.RED,
            "c" to ChatColor.RED,
            "light purple" to ChatColor.LIGHT_PURPLE,
            "light_purple" to ChatColor.LIGHT_PURPLE,
            "pink" to ChatColor.LIGHT_PURPLE,
            "d" to ChatColor.LIGHT_PURPLE,
            "yellow" to ChatColor.YELLOW,
            "e" to ChatColor.YELLOW,
            "white" to ChatColor.WHITE,
            "f" to ChatColor.WHITE,
            "random" to ChatColor.MAGIC,
            "magic" to ChatColor.MAGIC,
            "k" to ChatColor.MAGIC,
            "bold" to ChatColor.BOLD,
            "l" to ChatColor.BOLD,
            "strike" to ChatColor.STRIKETHROUGH,
            "strikethrough" to ChatColor.STRIKETHROUGH,
            "m" to ChatColor.STRIKETHROUGH,
            "underline" to ChatColor.UNDERLINE,
            "n" to ChatColor.UNDERLINE,
            "italic" to ChatColor.ITALIC,
            "o" to ChatColor.ITALIC,
            "reset" to ChatColor.RESET,
            "r" to ChatColor.RESET
    )

    fun send(sender: CommandSender, msg: String, vararg params: Any) {
        sender.sendMessage(parameterizeMessage(msg, params))
    }

    fun parseColor(input: String): ChatColor {
        return colors.getValue(input.toLowerCase().replace("&", ""))
    }

    private fun parameterizeMessage(msg: String, vararg params: Any): String {
        var message = "${ChatColor.YELLOW} + $msg"
        if (params.isNotEmpty())
            0.until(params.size).forEach { i: Int ->
                message = message.replace("$${i + 1}", "${ChatColor.WHITE} ${params[i]}${ChatColor.YELLOW}")
            }
        return message
    }
}