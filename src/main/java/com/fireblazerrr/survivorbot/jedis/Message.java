package com.fireblazerrr.survivorbot.jedis;

public class Message {

    private static String delimiter = "```";

    public String user;
    public String channel;
    public String message;
    public String source;

    public Message(String source, String channel, String user, String message) {
        this.source = source;
        this.user = user;
        this.channel = channel;
        this.message = message;
    }

    public Message(String message) {
        String[] args = message.split(delimiter, 4);
        this.source = args[0];
        this.channel = args[1];
        this.user = args[2];
        this.message = args[3];
    }

    @Override
    public String toString() {
        return source + delimiter + channel + delimiter + user + delimiter + message;
    }
}
