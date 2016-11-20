package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.spigot.command.Command;
import com.fireblazerrr.survivorbot.spigot.command.CommandHandler;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends BasicCommand {
    private static final int CMDS_PER_PAGE = 8;

    public HelpCommand() {
        super("Help");
        this.setDescription(this.getMessage("command_help"));
        this.setUsage("/ch help " + ChatColor.DARK_GRAY + "[page#]");
        this.setArgumentRange(0, 1);
        this.setIdentifiers("ch help", "survivorbot help");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        int page = 0;
        if (args.length != 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException ignored) {
            }
        }

        List sortCommands = SurvivorBot.getCommandHandler().getCommands();
        ArrayList commands = new ArrayList();

        for (Object sortCommand : sortCommands) {
            Command start = (Command) sortCommand;
            if (start.isShownOnHelpMenu() && CommandHandler.hasPermission(sender, start.getPermission())) {
                commands.add(start);
            }
        }

        int var13 = commands.size() / 8;
        if (commands.size() % 8 != 0) {
            ++var13;
        }

        if (var13 == 0) {
            var13 = 1;
        }

        if (page >= var13 || page < 0) {
            page = 0;
        }

        sender.sendMessage(
                ChatColor.RED + "-----[ " + ChatColor.WHITE + "SurvivorBot Help <" + (page + 1) + "/" + var13 + ">" + ChatColor.RED + " ]-----");
        int var14 = page * 8;
        int end = var14 + 8;
        if (end > commands.size()) {
            end = commands.size();
        }

        for (int c = var14; c < end; ++c) {
            Command cmd = (Command) commands.get(c);
            sender.sendMessage("  " + ChatColor.GREEN + "" + cmd.getUsage());
        }

        Messaging.send(sender, this.getMessage("help_moreInfo"), this.getMessage("help_infoCommand"));
        return true;
    }
}
