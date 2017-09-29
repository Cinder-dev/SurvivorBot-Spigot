package com.fireblazerrr.survivorbot.jedis

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import redis.clients.jedis.JedisPubSub
import java.util.logging.Logger

class JedisListener : JedisPubSub(){

    private val logger: Logger = Logger.getLogger("Minecraft")

    override fun onMessage(channel: String, message: String) {
        logger.info("JedisListener onMessage channel: $channel, message: $message")
        val jsonParser = JsonParser()
        val jsonObject: JsonObject = jsonParser.parse(message).asJsonObject

    }
}
