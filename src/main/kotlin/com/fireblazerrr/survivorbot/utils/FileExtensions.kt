package com.fireblazerrr.survivorbot.utils

import java.io.File

fun createIfMissing(location: String, folder: String): File{
    val loc = File(location, folder)
    loc.mkdirs()
    return loc
}