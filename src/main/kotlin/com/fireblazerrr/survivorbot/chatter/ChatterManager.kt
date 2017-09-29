package com.fireblazerrr.survivorbot.chatter

import org.bukkit.entity.Player

class ChatterManager{
    private val chatters: Map<String, Chatter> = emptyMap()
    private val chatterStorage: ChatterStorage = YMLChatterStorage()

    fun getChatter(player: Player) = chatters.getValue(player.name.toLowerCase())
}