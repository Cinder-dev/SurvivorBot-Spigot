package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

    public ReadyListener() {
    }

    @Override
    public void onReady(ReadyEvent event) {
        SurvivorBot.info("*** Discord bot armed ***");
        event.getJDA().getGuilds().forEach(guild -> {
            SurvivorBot.info("===== " + guild.getName() + " [" + guild.getId() + "] =====");
            SurvivorBot.info("///// Text Channels /////");
            guild.getTextChannels().forEach(textChannel -> SurvivorBot.info("      " + textChannel.getName() + " [" + textChannel.getId() + "]"));
            SurvivorBot.info("///// Voice Channels /////");
            guild.getVoiceChannels().forEach(voiceChannel -> SurvivorBot.info("      " + voiceChannel.getName() + " [" + voiceChannel.getId() + "]"));
            SurvivorBot.info("///// Roles /////");
            guild.getRoles().forEach(role -> SurvivorBot.info("      " + role.getName() + " [" + role.getId() + "]"));
            SurvivorBot.info("///// Users /////");
            guild.getMembers().forEach(member -> SurvivorBot.info("      " + member.getEffectiveName() + " [" + member.getUser().getId() + "]"));
            SurvivorBot.info("==========================================================================================");
        });
    }
}
