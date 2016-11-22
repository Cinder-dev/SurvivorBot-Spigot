package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import redis.clients.jedis.JedisPubSub;

public class JedisListener extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        if (channel.equals(SurvivorBot.getJedisManager().getChannel())) {

        }
    }

    public void onSubscribe(String channel, int subscribedChannels) {
        SurvivorBot.info("Now listening to " + channel + " for redis messages.");
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        SurvivorBot.info("Stopping listening to " + channel + " for redis messages.");
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPMessage(String pattern, String channel,
                           String message) {
    }
}
