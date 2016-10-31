package com.fireblazerrr.survivorbot.discord;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class ReadyListener implements IListener<ReadyEvent> {
    @Override
    public void handle(ReadyEvent event) {
        System.out.println("*** Discord discord armed ***");
        event.getClient().getGuilds().forEach(iGuild -> {
            System.out.println("===== " + iGuild.getName() + " \t\t[" + iGuild.getID() + "] =====");
            System.out.println("///// Channels /////");
            iGuild.getChannels().forEach(iChannel -> System.out.println("     " + iChannel.getName() + " \t\t[" + iChannel.getID() + "]"));
            System.out.println("///// Roles /////");
            iGuild.getRoles().forEach(iRole -> System.out.println("     " + iRole.getName() + " \t\t[" + iRole.getID() + "]"));
            System.out.println("///// Users - " + iGuild.getUsers().size() + " /////");
            if (iGuild.getUsers().size() < 100)
                iGuild.getUsers().forEach(iUser -> System.out.println("     " + iUser.getName() + " \t\t[" + iUser.getID() + "]"));
        });
    }
}
