package com.fireblazerrr.survivorbot.channel

interface ChannelStorage{

    fun addChannel(channel: Channel): Nothing
    fun flagUpdate(channel: Channel): Nothing
    fun load(string: String): Channel
    fun loadChannels(): Set<Channel>
    fun removeChannel(channel: Channel): Nothing
    fun update(): Nothing
    fun update(channel: Channel): Nothing
}