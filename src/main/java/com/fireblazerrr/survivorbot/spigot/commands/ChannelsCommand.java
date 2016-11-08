package com.fireblazerrr.survivorbot.spigot.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChannelsCommand implements CommandExecutor {

    private SurvivorBot plugin;

    public ChannelsCommand(SurvivorBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equals("ch") || s.equals("channels")) {
            switch (strings[0]){
                case "list": // List join-able channels and their command identities
                    break;
                case "join": // Join a channel
                    break;
                case "mute": // Mute a channel, resets on login
                    break;
                case "leave": // Leave a channel, permanently
                    break;
            }
        }
        return false;
    }
}
