package com.fireblazerrr.survivorbot.jedis;

import com.fireblazerrr.survivorbot.SurvivorBot;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class JedisManager implements Runnable {

    private String hostname = "";
    private int port = 6379;
    private String password = "";
    private final String channel = "SURVIVOR_BOT_CHAT_CHANNEL";

    private static JedisPool pool;

    @Override
    public void run() {

        SurvivorBot.info("REDIS HOSTNAME: " + hostname);
        SurvivorBot.info("REDIS PORT: " + port);
        SurvivorBot.info("REDIS PASSWORD: " + password);

        if (password.equals("")) {
            pool = new JedisPool(
                    new JedisPoolConfig(),
                    hostname,
                    port,
                    Protocol.DEFAULT_TIMEOUT
            );
        } else {
            pool = new JedisPool(
                    new JedisPoolConfig(),
                    hostname,
                    port,
                    Protocol.DEFAULT_TIMEOUT,
                    password
            );
        }

        // start listening
        pool.getResource().subscribe(new JedisListener(), channel);


    }

    /**
     * Send message to Redis
     */
    public static void publish(String message) {
        pool.getResource().publish("", message);
    }

    public void destroy() {
        pool.getResource().close();
        pool.destroy();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannel() {
        return channel;
    }

    public JedisPool getPool() {
        return pool;
    }
}
