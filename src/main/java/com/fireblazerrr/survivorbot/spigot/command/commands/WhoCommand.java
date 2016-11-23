package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class WhoCommand extends BasicCommand {
    public WhoCommand() {
        super("Who");
        this.setDescription(this.getMessage("command_who"));
        this.setUsage("/ch who " + ChatColor.DARK_GRAY + "[channel]");
        this.setArgumentRange(0, 1);
        this.setIdentifiers("ch who", "survivorbot who");
        this.setNotes(
                ChatColor.RED + "Note:" + ChatColor.YELLOW + " If no channel is given, your active channel is used.");
    }

    public boolean execute(CommandSender sender, String identifier, String[] args) {
        Chatter chatter = null;
        Channel channel;

        if (sender instanceof Player)
            chatter = SurvivorBot.getChatterManager().getChatter((Player) sender);

        if (args.length == 0) {
            if (chatter != null)
                channel = chatter.getActiveChannel();
            else
                channel = SurvivorBot.getChannelManager().getDefaultChannel();
        } else
            channel = SurvivorBot.getChannelManager().getChannel(args[0]);


        if (channel == null) {
            Messaging.send(sender, this.getMessage("who_noChannel"));
            return true;
        } else {

            List<Chatter> mem = channel.getMembers().stream().collect(Collectors.toList());

            mem.stream().filter(chatter1 -> sender instanceof Player && !((Player) sender).canSee(chatter1.getPlayer())).forEachOrdered(chatter1 -> {

            });

            ArrayList<String> members = new ArrayList<>();
            Iterator count = channel.getMembers().iterator();

            while (true) {
                Chatter line;
                do {
                    if (!count.hasNext()) {
                        members.sort(String::compareToIgnoreCase);
                        sender.sendMessage(ChatColor.RED + "------------[ " + channel.getColor() + channel
                                .getName() + ChatColor.RED + " ]------------");
                        int memberCount = members.size();
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < memberCount; ++i) {
                            String name = members.get(i);
                            if (channel.isMuted(name)) {
                                name = ChatColor.RED + name + ChatColor.WHITE;
                            } else if (channel.isModerator(name)) {
                                name = ChatColor.GREEN + name + ChatColor.WHITE;
                            }

                            if (i + 1 < memberCount) {
                                name = name + ", ";
                            }

                            if (stringBuilder.length() + name.length() > 64) {
                                sender.sendMessage(stringBuilder.toString().trim());
                                stringBuilder = new StringBuilder();
                                --i;
                            } else {
                                stringBuilder.append(name);
                                if (i + 1 == memberCount) {
                                    sender.sendMessage(stringBuilder.toString().trim());
                                }
                            }
                        }

                        return true;
                    }

                    line = (Chatter) count.next();
                } while (sender instanceof Player && !((Player) sender).canSee(line.getPlayer()));

                members.add(line.getPlayer().getName());
            }
        }
    }
}
