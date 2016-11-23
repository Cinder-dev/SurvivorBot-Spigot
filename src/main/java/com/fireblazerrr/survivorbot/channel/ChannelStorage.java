package com.fireblazerrr.survivorbot.channel;

import java.util.Set;

public interface ChannelStorage {

    void addChannel(Channel channel);

    void flagUpdate(Channel channel);

    Channel load(String string);

    Set<Channel> loadChannels();

    void removeChannel(Channel channel);

    void update();

    void update(Channel channel);
}
