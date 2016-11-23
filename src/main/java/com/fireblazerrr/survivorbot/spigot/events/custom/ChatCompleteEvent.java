package com.fireblazerrr.survivorbot.spigot.events.custom;

import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Chatter sender;
    private final Channel channel;
    private final String msg;

    public ChatCompleteEvent(Chatter sender, Channel channel, String msg) {
        this.sender = sender;
        this.channel = channel;
        this.msg = msg;
    }

    public Chatter getSender() {
        return this.sender;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public String getMsg() {
        return this.msg;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
