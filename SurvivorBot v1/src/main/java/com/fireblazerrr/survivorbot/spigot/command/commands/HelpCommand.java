package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.spigot.command.Command;
import com.fireblazerrr.survivorbot.spigot.command.CommandHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        int maxPages = commands.size() / 8;
        if (commands.size() % 8 != 0) {
            ++maxPages;
        }

        if (maxPages == 0) {
            maxPages = 1;
        }

        if (page >= maxPages || page < 0) {
            page = 0;
        }

        TextComponent master = new TextComponent("\n\n");
        TextComponent prev;
        TextComponent next;
        TextComponent title;

        if (page == 0) {
            prev = new TextComponent("             [");
            next = new TextComponent("] [NEXT PAGE] ");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch help " + (page + 2)));
            next.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
        } else if (page + 1 == maxPages) {
            prev = new TextComponent(" [PREV PAGE] [");
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch help " + page));
            prev.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
            next = new TextComponent("]             ");
        } else {
            prev = new TextComponent(" [PREV PAGE] [");
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch help " + page));
            prev.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
            next = new TextComponent("] [NEXT PAGE] ");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch help " + (page + 2)));
            next.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
        }
        prev.setColor(net.md_5.bungee.api.ChatColor.RED);
        next.setColor(net.md_5.bungee.api.ChatColor.RED);

        title = new TextComponent(" SurvivorBot Help <" + (page + 1) + "/" + maxPages + "> ");
        title.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        master.addExtra(prev);
        master.addExtra(title);
        master.addExtra(next);

        ((Player) sender).spigot().sendMessage(master);

        int commandOffset = page * 8;
        int end = commandOffset + 8;
        if (end > commands.size()) {
            end = commands.size();
        }

        for (int c = commandOffset; c < end; ++c) {
            Command cmd = (Command) commands.get(c);

            TextComponent cmdComponent = new TextComponent("  " + cmd.getUsage());

            ComponentBuilder helpComponent = new ComponentBuilder(
                    ChatColor.RED + "Name: " + ChatColor.YELLOW + cmd.getName() + "\n");
            helpComponent.append(ChatColor.RED + "Description: " + ChatColor.YELLOW + cmd.getDescription() + "\n");
            helpComponent.append(ChatColor.RED + "Usage: " + ChatColor.YELLOW + cmd.getUsage() + "\n");
            helpComponent.append(ChatColor.DARK_GRAY + "Click to copy to chat bar.");

            cmdComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            cmdComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, helpComponent.create()));
            cmdComponent.setClickEvent(
                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ChatColor.stripColor(cmd.getUsage())));

            ((Player) sender).spigot().sendMessage(cmdComponent);
        }
        return true;
    }
}
