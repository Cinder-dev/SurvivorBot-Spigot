package com.fireblazerrr.survivorbot.chatter;

public interface ChatterStorage {

    void flagUpdate(Chatter chatter);

    Chatter load(String string);

    void removeChatter(Chatter chatter);

    void update();

    void update(Chatter chatter);

}
