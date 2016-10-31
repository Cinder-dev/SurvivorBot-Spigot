package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager implements Runnable {

    JedisPool pool;

    @Override
    public void run() {
        // Get channels to be listened to from the config
        String channels = SurvivorBot.configManager.pluginConfig.getString("channels");

        pool = new JedisPool(new JedisPoolConfig(), "localhost");

        // start listening
        pool.getResource().subscribe(new JedisListener(), channels.split(" "));

    }

    public void destroy() {
        pool.destroy();
    }
}
