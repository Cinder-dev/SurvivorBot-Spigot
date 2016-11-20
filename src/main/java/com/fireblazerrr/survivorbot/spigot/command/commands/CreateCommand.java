package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.channel.StandardChannel;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends BasicCommand {

    public CreateCommand() {
        super("Create Channel");
        this.setDescription(this.getMessage("command_create"));
        this.setUsage("/ch create " + ChatColor.DARK_GRAY + "<name> [nick]");
        this.setArgumentRange(1, 2);
        this.setIdentifiers("ch create", "survivorbot create");
        this.setPermission("survivorbot.create");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        String name = args[0];
        ChannelManager channelManager = SurvivorBot.getChannelManager();
        if (channelManager.hasChannel(name)) {
            Messaging.send(sender, this.getMessage("create_nameTaken"));
            return true;
        } else if (!name.matches("[a-zA-Z0-9]+")) {
            Messaging.send(sender, this.getMessage("create_nameInvalid"));
            return true;
        } else {
            String nick;
            if (args.length == 2) {
                nick = args[1];
                if (!nick.matches("[a-zA-Z0-9]+")) {
                    Messaging.send(sender, this.getMessage("create_nickInvalid"));
                    return true;
                }
            } else {
                nick = name;

                for (int channel = 0; channel < name.length(); ++channel) {
                    nick = name.substring(0, channel + 1);
                    if (!channelManager.hasChannel(nick)) {
                        break;
                    }
                }
            }

            if (channelManager.hasChannel(nick)) {
                Messaging.send(sender, "create_nickTaken");
                return true;
            } else {
                StandardChannel standardChannel = new StandardChannel(channelManager.getStorage(), name, nick,
                        channelManager);
                if (sender instanceof Player) {
                    standardChannel.setModerator(sender.getName(), true);
                }

                channelManager.addChannel(standardChannel);
                channelManager.getStorage().update(standardChannel);
                Messaging.send(sender, this.getMessage("create_confirm"));
                return true;
            }
        }
    }
}
