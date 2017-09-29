package com.fireblazerrr.survivorbot.chatter

import java.util.*

interface ChatterStorage{

    fun flagUpdate(chatter: Chatter): Nothing
    fun load(uuid: UUID): Chatter
    fun removeChatter(chatter: Chatter): Nothing
    fun update(): Nothing
    fun update(chatter: Chatter): Nothing
}