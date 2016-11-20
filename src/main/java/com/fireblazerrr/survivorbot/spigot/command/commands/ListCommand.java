package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListCommand extends BasicCommand {
    private static final int CHANNELS_PER_PAGE = 8;

    public ListCommand() {
        super("List");
        this.setDescription(this.getMessage("command_list"));
        this.setUsage("/ch list " + ChatColor.DARK_GRAY + "[page#]");
        this.setArgumentRange(0, 1);
        this.setIdentifiers("ch list", "survivorbot list");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        int page = 0;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException ignored) {
            }
        }

        ArrayList channels = SurvivorBot.getChannelManager().getChannels().stream()
                .filter(numPages -> !numPages.isHidden() && SurvivorBot
                        .hasChannelPermission((Player) sender, numPages, Chatter.Permission.JOIN))
                .collect(Collectors.toCollection(ArrayList::new));

        Chatter var14 = null;
        if (sender instanceof Player) {
            var14 = SurvivorBot.getChatterManager().getChatter((Player) sender);
        }

        int var15 = channels.size() / 8;
        if (channels.size() % 8 != 0) {
            ++var15;
        }

        if (var15 == 0) {
            var15 = 1;
        }

        if (page >= var15 || page < 0) {
            page = 0;
        }

        sender.sendMessage(
                ChatColor.RED + "-----[ " + ChatColor.WHITE + "Survivorbot Channels <" + (page + 1) + "/" + var15 + ">" + ChatColor.RED + " ]-----");
        int start = page * 8;
        int end = start + 8;
        if (end > channels.size()) {
            end = channels.size();
        }

        for (int c = start; c < end; ++c) {
            Channel channel = (Channel) channels.get(c);
            String line = channel.getColor() + "  [" + channel.getNick() + "] " + channel.getName();
            if (var14 != null && channel.isMember(var14)) {
                line = line + "*";
            }

            sender.sendMessage(line);
        }

        return true;
    }
}
