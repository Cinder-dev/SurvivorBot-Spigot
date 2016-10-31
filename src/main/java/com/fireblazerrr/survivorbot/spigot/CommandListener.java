package com.fireblazerrr.survivorbot.spigot;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.jedis.JedisListener;
import com.fireblazerrr.survivorbot.jedis.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sx.blah.discord.handle.obj.Presences;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        switch (command.getName()) {
            case "channels":
                if (commandSender.hasPermission("survivorbot.channels")) {
                    commandSender.sendMessage(SurvivorBot.configManager.pluginConfig.getString("channels"));
                    return true;
                }
                break;
            case "online":
                if (commandSender.hasPermission("survivorbot.online")) {
                    if (SurvivorBot.configManager.discordConfig.getString("serverID") != null) {
                        SurvivorBot.instance.client.getGuildByID(SurvivorBot.configManager.discordConfig.getString("serverID")).getUsers().forEach(iUser -> {
                            if (iUser.getPresence().equals(Presences.ONLINE)) {
                                commandSender.sendMessage(iUser.getName());
                            }
                        });
                        return true;
                    }
                }
                break;
            case "ranks":
                if (commandSender.hasPermission("survivorbot.ranks")) {
                    if (SurvivorBot.configManager.discordConfig.getString("serverID") != null) {
                        SurvivorBot.instance.client.getGuildByID(SurvivorBot.configManager.discordConfig.getString("serverID")).getRoles().forEach(iRole -> {
                            commandSender.sendMessage(iRole.getName());
                        });
                        return true;
                    }
                }
                break;
            case "s":
                if (commandSender.isOp() || commandSender.hasPermission("survivorbot.staff")) {
                    if (strings.length > 0) {
                        String message;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < strings.length; i++){
                            sb.append(strings[i] + " ");
                        }
                        message = sb.toString();
                        JedisListener.publish(SurvivorBot.configManager.discordConfig.getString("staffChannel"), new Message(
                                "staff",
                                SurvivorBot.configManager.discordConfig.getString("staffChannel"),
                                commandSender instanceof Player ? commandSender.getName() : "",
                                message
                        ));
                        return true;
                    }
                }
        }
        return false;
    }
}
