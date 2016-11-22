package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class ReadyListener implements IListener<ReadyEvent> {

    public ReadyListener() {

    }

    @Override
    public void handle(ReadyEvent event) {
        SurvivorBot.info("*** Discord discord armed ***");
        event.getClient().getGuilds().forEach(iGuild -> {
            SurvivorBot.info("===== " + iGuild.getName() + " \t\t[" + iGuild.getID() + "] =====");
            SurvivorBot.info("///// Channels /////");
            iGuild.getChannels().forEach(iChannel -> SurvivorBot.info("     " + iChannel.getName() + " \t\t[" + iChannel.getID() + "]"));
            SurvivorBot.info("///// Roles /////");
            iGuild.getRoles().forEach(iRole -> SurvivorBot.info("     " + iRole.getName() + " \t\t[" + iRole.getID() + "]"));
            SurvivorBot.info("///// Users - " + iGuild.getUsers().size() + " /////");
            if (iGuild.getUsers().size() < 100)
                iGuild.getUsers().forEach(iUser -> SurvivorBot.info("     " + iUser.getName() + " \t\t[" + iUser.getID() + "]"));
            SurvivorBot.info("================================================================");
        });
    }
}
