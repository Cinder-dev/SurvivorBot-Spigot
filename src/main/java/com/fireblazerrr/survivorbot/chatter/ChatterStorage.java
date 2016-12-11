package com.fireblazerrr.survivorbot.chatter;

import java.util.UUID;

public interface ChatterStorage {

    void flagUpdate(Chatter chatter);

    Chatter load(UUID userUID);

    void removeChatter(Chatter chatter);

    void update();

    void update(Chatter chatter);

}
