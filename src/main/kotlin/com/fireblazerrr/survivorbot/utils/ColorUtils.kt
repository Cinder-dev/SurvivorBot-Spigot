package com.fireblazerrr.survivorbot.utils

import net.md_5.bungee.api.ChatColor
import java.awt.Color

class ColorUtils {
    private val colorList: List<ColorName> = listOf(
            ColorName(ChatColor.BLACK, 0, 0, 0),
            ColorName(ChatColor.DARK_BLUE, 0, 0, 170),
            ColorName(ChatColor.DARK_GREEN, 0, 170, 0),
            ColorName(ChatColor.DARK_AQUA, 0, 170, 170),
            ColorName(ChatColor.DARK_RED, 170, 0, 0),
            ColorName(ChatColor.DARK_PURPLE, 170, 0, 170),
            ColorName(ChatColor.GOLD, 255, 170, 0),
            ColorName(ChatColor.GRAY, 170, 170, 170),
            ColorName(ChatColor.DARK_GRAY, 85, 85, 85),
            ColorName(ChatColor.BLUE, 85, 85, 255),
            ColorName(ChatColor.GREEN, 85, 255, 85),
            ColorName(ChatColor.AQUA, 85, 255, 255),
            ColorName(ChatColor.RED, 255, 85, 85),
            ColorName(ChatColor.LIGHT_PURPLE, 255, 58, 255),
            ColorName(ChatColor.YELLOW, 255, 255, 85),
            ColorName(ChatColor.WHITE, 255, 255, 255)
    )

    fun getColorNameFromRgb(r: Int, g: Int, b: Int): ChatColor {
        var closestMatch = ColorName(ChatColor.WHITE, 255, 255, 255)
        var minMSE: Int = Integer.MAX_VALUE
        var mse: Int
        colorList.forEach {
            mse = it.computeMSE(r, g, b)
            if (mse < minMSE) {
                minMSE = mse
                closestMatch = it
            }
        }

        return closestMatch.name
    }

    fun getColorNameFromHex(hexColor: Int): ChatColor {
        val r = hexColor and 0xFF0000 shr 16
        val g = hexColor and 0xFF00 shr 8
        val b = hexColor and 0xFF
        return getColorNameFromRgb(r, g, b)
    }

    fun colorToHex(c: Color): Int = Integer.decode("0x" + Integer.toHexString(c.rgb).substring(2))!!


    fun getColorNameFromColor(color: Color?): ChatColor = if (color == null)
        ChatColor.WHITE
    else
        getColorNameFromRgb(color.red, color.green, color.blue)

}

class ColorName(val name: ChatColor, val r: Int, val g: Int, val b: Int) {

    fun computeMSE(pixR: Int, pixG: Int, pixB: Int): Int = (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
            * (pixB - b)) / 3)
}