package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.chatter.ChatterManager;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ReloadCommand extends BasicCommand {
    public ReloadCommand() {
        super("Reload");
        this.setDescription(this.getMessage("command_reload"));
        this.setUsage("/ch reload");
        this.setArgumentRange(0, 0);
        this.setIdentifiers("ch reload", "survivorchat reload");
        this.setPermission("survivorbot.reload");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        ChannelManager channelManager = SurvivorBot.getChannelManager();
        ChatterManager chatterManager = SurvivorBot.getChatterManager();
        channelManager.clear();
        chatterManager.clear();
        SurvivorBot.getPlugin().setupStorage();
        channelManager.loadChannels();

        SurvivorBot.getConfigManager().load(new File(SurvivorBot.getPlugin().getDataFolder(), "config.yml"));


        channelManager.getStorage().update();
        chatterManager.reset();

        Bukkit.getOnlinePlayers().forEach(chatterManager::addChatter);

        Messaging.send(sender, this.getMessage("reload_confirm"));
        return true;
    }
}
