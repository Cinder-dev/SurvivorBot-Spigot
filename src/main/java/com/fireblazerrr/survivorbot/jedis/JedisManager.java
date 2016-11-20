package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import redis.clients.jedis.JedisPool;

public class JedisManager implements Runnable {

    private JedisPool pool;

    public JedisManager(SurvivorBot plugin) {
        SurvivorBot plugin1 = plugin;
    }

    @Override
    public void run() {

//        Log.info("REDIS: " + plugin.config.getString("plugin.general.redis.host"));
//        Log.info("REDIS: " + plugin.config.getInt("plugin.general.redis.port"));
//        Log.info("REDIS: " + plugin.config.getString("plugin.general.redis.password"));
//        Log.info("REDIS: " + plugin.config.getString("plugin.general.redis.channel"));
//
//        if (plugin.config.getString("plugin.general.redis.password").equals("") || plugin.config.getString("plugin.general.redis.password") == null) {
//            pool = new JedisPool(
//                    new JedisPoolConfig(),
//                    plugin.config.getString("plugin.general.redis.host"),
//                    plugin.config.getInt("plugin.general.redis.port"),
//                    Protocol.DEFAULT_TIMEOUT
//            );
//        } else {
//            pool = new JedisPool(
//                    new JedisPoolConfig(),
//                    plugin.config.getString("plugin.general.redis.host"),
//                    plugin.config.getInt("plugin.general.redis.port"),
//                    Protocol.DEFAULT_TIMEOUT,
//                    plugin.config.getString("plugin.general.redis.password")
//            );
//        }
//
//        // start listening
//        pool.getResource().subscribe(new JedisListener(plugin), plugin.config.getString("plugin.general.redis.channel"));


    }

    public void destroy() {
        pool.destroy();
    }
}
