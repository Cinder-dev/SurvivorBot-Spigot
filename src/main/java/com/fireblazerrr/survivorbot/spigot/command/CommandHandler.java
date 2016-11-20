package com.fireblazerrr.survivorbot.spigot.command;

import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler {
    private Map<String, Command> commands = new LinkedHashMap<>();
    private Map<String, Command> identifiers = new HashMap<>();

    public CommandHandler() {
    }

    public void addCommand(Command command) {
        this.commands.put(command.getName().toLowerCase(), command);

        Arrays.stream(command.getIdentifiers()).forEach(s -> identifiers.put(s.toLowerCase(), command));
    }

    public boolean dispatch(CommandSender sender, String lable, String[] args) {
        for (int argsIncluded = args.length; argsIncluded >= 0; --argsIncluded) {
            StringBuilder identifier = new StringBuilder(lable);

            for (int cmd = 0; cmd < argsIncluded; ++cmd) {
                identifier.append(" ").append(args[cmd]);
            }

            Command command = this.getCmdFromIdent(identifier.toString(), sender);
            if (command != null) {
                String[] realArgs = Arrays.copyOfRange(args, argsIncluded, args.length);
                if (!command.isInProgress(sender)) {
                    if (realArgs.length < command.getMinArguments() || realArgs.length > command.getMaxArguments()) {
                        this.displayCommandHelp(command, sender);
                        return true;
                    }

                    if (realArgs.length > 0 && realArgs[0].equals("?")) {
                        this.displayCommandHelp(command, sender);
                        return true;
                    }
                }

                if (!command.getPermission().isEmpty() && !sender.hasPermission(command.getPermission())) {
                    Messaging.send(sender, "Insufficient permission.");
                    return true;
                }

                command.execute(sender, identifier.toString(), realArgs);
                return true;
            }
        }

        return true;
    }

    private void displayCommandHelp(Command cmd, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "----- " + ChatColor.WHITE + "[ Command Help ]" + ChatColor.RED + "-----");
        sender.sendMessage(ChatColor.RED + "Command:" + ChatColor.YELLOW + " " + cmd.getName());
        sender.sendMessage(ChatColor.RED + "Description:" + ChatColor.YELLOW + " " + cmd.getDescription());
        sender.sendMessage(ChatColor.RED + "Usage:" + ChatColor.YELLOW + " " + cmd.getUsage());
        if (cmd.getNotes() != null) {
            Arrays.stream(cmd.getNotes()).forEach(s -> sender.sendMessage(ChatColor.YELLOW + s));
        }
    }

    public Command getCmdFromIdent(String ident, CommandSender executor) {
        ident = ident.toLowerCase();
        if (this.identifiers.containsKey(ident)) {
            return this.identifiers.get(ident);
        } else {
            Iterator i = this.commands.values().iterator();

            Command cmd;
            do {
                if (!i.hasNext())
                    return null;
                cmd = (Command) i.next();
            } while (!cmd.isIdentifier(executor, ident));

            return cmd;
        }
    }

    public Command getCommand(String name) {
        return this.commands.get(name.toLowerCase());
    }

    public List<Command> getCommands() {
        return new ArrayList<>(this.commands.values());
    }

    public void removeCommand(Command command) {
        this.commands.remove(command.getName().toLowerCase());
        Arrays.stream(command.getIdentifiers()).forEach(s -> this.identifiers.remove(s.toLowerCase()));
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender.isOp()) {
            return true;
        } else if (sender instanceof Player && permission != null && !permission.isEmpty()) {
            Player player = (Player) sender;
            return player.hasPermission(permission);
        } else {
            return true;
        }
    }
}
