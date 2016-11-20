package com.fireblazerrr.survivorbot.jedis;

//import com.fireblazerrr.survivorbot.SurvivorBot;
//import com.fireblazerrr.survivorbot.utils.message.Message;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPubSub;
//
//public class JedisListener extends JedisPubSub {
//
//    JedisListener(SurvivorBot plugin) {
//        SurvivorBot plugin1 = plugin;
//    }
//
//    @Override
//    public void onMessage(String channel, String message) {
////        if (channel.equals(plugin.config.getString("plugin.general.redis.channel"))) {
////            Message received = new Message(message);
////
////            if (received.source.equals("discord")) {
////                new DiscordSource(plugin, received);
////            } else {
////                new MinecraftSource(plugin, received);
////            }
////        }
//    }
//
//    public void onSubscribe(String channel, int subscribedChannels) {
//    }
//
//    public void onUnsubscribe(String channel, int subscribedChannels) {
//    }
//
//    public void onPSubscribe(String pattern, int subscribedChannels) {
//    }
//
//    public void onPUnsubscribe(String pattern, int subscribedChannels) {
//    }
//
//    public void onPMessage(String pattern, String channel,
//                           String message) {
//    }
//
//    /**
//     * Send message to Redis
//     */
//    public static void publish(String channel, Message message) {
//        Jedis jedis = new Jedis("localhost");
//        jedis.publish(channel, message.toString());
//        jedis.close();
//    }
//}
