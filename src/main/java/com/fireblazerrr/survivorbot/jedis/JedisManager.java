package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager implements Runnable {

    private SurvivorBot plugin;

    public JedisManager(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    JedisPool pool;

    @Override
    public void run() {
        // Get channels to be listened to from the config
        String channels = plugin.config.getString("plugin.chat.channels");

        pool = new JedisPool(new JedisPoolConfig(), "localhost");

        // start listening
        pool.getResource().subscribe(new JedisListener(plugin), channels.split(" "));

    }

    public void destroy() {
        pool.destroy();
    }
}
