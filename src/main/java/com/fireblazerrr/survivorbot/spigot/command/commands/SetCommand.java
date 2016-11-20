package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends BasicCommand {
    public SetCommand() {
        super("Set Channel Setting");
        this.setDescription(this.getMessage("command_set"));
        this.setUsage("/ch set " + ChatColor.DARK_GRAY + "<channel> <setting> <value>");
        this.setArgumentRange(3, 3);
        this.setIdentifiers("ch set", "survivorbot set");
        this.setNotes(
                ChatColor.RED + "Settings:" + ChatColor.YELLOW + " nick, format, password, distance, color, shortcut, verbose, crossworld, chatcost");
        this.setNotes(ChatColor.RED + "Note:" + ChatColor.YELLOW + " setting the password to \'none\' clears it");
    }

    private static boolean argToBoolean(String arg) {
        arg = arg.toLowerCase();
        return arg.equals("1") || arg.equals("t") || arg.equals("true") || arg.equals("y") || arg.equals("yes") || arg
                .equals("on");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        String name = args[0];
        String setting = args[1].toLowerCase();
        String value = args[2];
        ChannelManager channelMngr = SurvivorBot.getChannelManager();
        Channel channel = channelMngr.getChannel(name);
        if (channel == null) {
            Messaging.send(sender, this.getMessage("set_noChannel"));
            return true;
        } else {
            if (sender instanceof Player) {
                Player color = (Player) sender;
                Chatter chatter = SurvivorBot.getChatterManager().getChatter(color);
                if (chatter.canModify(setting, channel) != Chatter.Result.ALLOWED) {
                    Messaging.send(sender, this.getMessage("set_noPermission"), setting,
                            channel.getColor() + channel.getName());
                    return true;
                }
            }

            switch (setting) {
                case "nick":
                    if (channelMngr.hasChannel(value)) {
                        Messaging.send(sender, this.getMessage("set_identifierTaken"));
                    } else {
                        channel.setNick(value);
                        channelMngr.removeChannel(channel);
                        channelMngr.addChannel(channel);
                        Messaging.send(sender, this.getMessage("set_confirmNick"));
                    }
                    break;
                case "format":
                    channel.setFormat(value);
                    Messaging.send(sender, this.getMessage("set_confirmFormat"));
                    break;
                case "password":
                    if (setting.equals("none")) {
                        channel.setPassword(null);
                    } else {
                        channel.setPassword(value);
                    }

                    Messaging.send(sender, this.getMessage("set_confirmPassword"));
                    break;
                case "distance":
                    try {
                        int color1 = Integer.parseInt(value);
                        channel.setDistance(color1);
                        Messaging.send(sender, this.getMessage("set_confirmDistance"));
                    } catch (NumberFormatException var11) {
                        Messaging.send(sender, this.getMessage("set_badDistance"));
                    }
                    break;
                case "color":
                    ChatColor color2 = Messaging.parseColor(value);
                    if (color2 == null) {
                        Messaging.send(sender, this.getMessage("set_badColor"));
                    } else {
                        channel.setColor(color2);
                        Messaging.send(sender, this.getMessage("set_confirmColor"));
                    }
                    break;
                case "shortcut":
                    if (argToBoolean(value)) {
                        channel.setShortcutAllowed(true);
                        Messaging.send(sender, this.getMessage("set_enableQuickmsg"));
                    } else {
                        channel.setShortcutAllowed(false);
                        Messaging.send(sender, this.getMessage("set_disableQuickmsg"));
                    }
                    break;
                case "verbose":
                    if (argToBoolean(value)) {
                        channel.setVerbose(true);
                        Messaging.send(sender, this.getMessage("set_enableVerbose"));
                    } else {
                        channel.setVerbose(false);
                        Messaging.send(sender, this.getMessage("set_disableVerbose"));
                    }
                    break;
                case "crossworld":
                    if (argToBoolean(value)) {
                        channel.setCrossWorld(true);
                        Messaging.send(sender, this.getMessage("set_enableCrossworld"));
                    } else {
                        channel.setCrossWorld(false);
                        Messaging.send(sender, this.getMessage("set_disableCrossworld"));
                    }
                    break;
                case "muted":
                    if (argToBoolean(value)) {
                        channel.announce(this.getMessage("set_enableMute"));
                        channel.setMuted(true);
                        Messaging.send(sender, this.getMessage("set_enableMute"));
                    } else {
                        channel.announce(this.getMessage("set_disableMute"));
                        channel.setMuted(false);
                        Messaging.send(sender, this.getMessage("set_disableMute"));
                    }
                    break;
            }

            return true;
        }
    }
}
