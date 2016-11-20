package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import com.fireblazerrr.survivorbot.utils.message.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AFKCommand extends BasicCommand {
    public AFKCommand() {
        super("AFK");
        this.setDescription(this.getMessage("command_afk"));
        this.setUsage("/afk [message]");
        this.setArgumentRange(0, 2137483647);
        this.setIdentifiers("afk", "ch afk", "survivorbot afk");
    }

    public boolean execute(CommandSender sender, String identifiers, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            Chatter chatter = SurvivorBot.getChatterManager().getChatter(player);
            if (chatter.isAFK()) {
                chatter.setAFK(false);
                chatter.setAFKMessage("");
                Messaging.send(player, this.getMessage("afk_disable"));
            } else {
                chatter.setAFK(true);
                if (args.length > 1) {
                    StringBuilder msg = new StringBuilder();

                    Arrays.stream(args).forEach(s -> msg.append(s).append(" "));

                    chatter.setAFKMessage(msg.toString().trim());
                }

                Messaging.send(player, this.getMessage("afk_enabled"));
            }

            return true;
        }
    }
}
