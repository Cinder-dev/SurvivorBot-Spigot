package com.fireblazerrr.survivorbot.spigot.command.commands;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.spigot.command.BasicCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

        ArrayList channels;

        channels = SurvivorBot.getChannelManager().getChannels().stream()
                .filter(numPages ->
                        !numPages.isHidden() && SurvivorBot.hasChannelPermission((Player) sender, numPages, Chatter.Permission.JOIN))
                .collect(Collectors.toCollection(ArrayList::new));

        Chatter chatter = SurvivorBot.getChatterManager().getChatter((Player) sender);

        int channelsSize = channels.size() / 8;
        if (channels.size() % 8 != 0) {
            ++channelsSize;
        }

        if (channelsSize == 0) {
            channelsSize = 1;
        }

        if (page >= channelsSize || page < 0) {
            page = 0;
        }

        TextComponent master = new TextComponent("\n\n");
        TextComponent prev;
        TextComponent next;
        TextComponent title;

        if (page == 0) {
            prev = new TextComponent("             [");

            if (channelsSize != 1) {
                next = new TextComponent("] [NEXT PAGE] ");
                next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch list " + page + 2));
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
            } else {
                next = new TextComponent("]             ");
            }
        } else if (page + 1 == channelsSize) {
            prev = new TextComponent(" [PREV PAGE] [");
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch list " + page));
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));

            next = new TextComponent("]             ");
        } else {
            prev = new TextComponent(" [PREV PAGE] [");
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch list " + page));
            prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));

            next = new TextComponent("] [NEXT PAGE] ");
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch list " + page + 2));
            next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Change Page").create()));
        }
        prev.setColor(net.md_5.bungee.api.ChatColor.RED);
        next.setColor(net.md_5.bungee.api.ChatColor.RED);

        title = new TextComponent("Survivorbot Channels <" + (page + 1) + "/" + channelsSize + ">");
        title.setColor(net.md_5.bungee.api.ChatColor.WHITE);

        master.addExtra(prev);
        master.addExtra(title);
        master.addExtra(next);

        ((Player) sender).spigot().sendMessage(master);
        int start = page * 8;
        int end = start + 8;
        if (end > channels.size()) {
            end = channels.size();
        }

        for (int c = start; c < end; ++c) {
            Channel channel = (Channel) channels.get(c);

            TextComponent section = new TextComponent();
            TextComponent channelView = new TextComponent("  [" + channel.getNick() + "] " + channel.getName());
            channelView.setColor(channel.getColor().asBungee());
            if (chatter != null && chatter.canViewInfo(channel) == Chatter.Result.ALLOWED) {
                ComponentBuilder channelViewHover = new ComponentBuilder("");


                channelViewHover.append(ChatColor.RED + "------------[ " + channel.getColor() + channel.getName() + ChatColor.RED + " ]------------\n");
                channelViewHover.append(ChatColor.YELLOW + "Name: " + channel.getColor() + channel.getName() + "\n");
                channelViewHover.append(ChatColor.YELLOW + "Nick: " + channel.getColor() + channel.getNick() + "\n");
                channelViewHover.append(ChatColor.YELLOW + "Format: " + ChatColor.WHITE + channel.getFormat() + "\n");
                if (!channel.getPassword().isEmpty()) {
                    channelViewHover.append(ChatColor.YELLOW + "Password Protected: " + ChatColor.WHITE + "Yes" + "\n");
                }

                if (channel.getDistance() > 0) {
                    channelViewHover.append(ChatColor.YELLOW + "Distance: " + ChatColor.WHITE + channel.getDistance() + "\n");
                }

                channelViewHover.append(
                        ChatColor.YELLOW + "Shortcut Allowed: " + ChatColor.WHITE + (channel.isShortcutAllowed() ? "true" : "false"));


                channelView.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, channelViewHover.create()));
            }

            int spaces = 25;
            spaces -= channelView.getText().length();
            StringBuilder joinPadding = new StringBuilder();
            for (int i = 0; i < spaces; i++)
                joinPadding.append(" ");
            channelView.setText(channelView.getText() + joinPadding.toString());

            TextComponent focus = new TextComponent("[FOCUS]");
            ComponentBuilder focusHover = new ComponentBuilder(ChatColor.RED + "Click to focus " + channel.getColor() + channel.getName());
            focus.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch " + channel.getName()));
            focus.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, focusHover.create()));
            focus.setColor(net.md_5.bungee.api.ChatColor.AQUA);

            TextComponent join = new TextComponent(" [JOIN]");
            ComponentBuilder joinHover = new ComponentBuilder(ChatColor.RED + "Click to join " + channel.getColor() + channel.getName());
            if (channel.getPassword().equals("")) {
                join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch join " + channel.getName()));
            } else {
                joinHover.append(ChatColor.DARK_GRAY + " : Password Required");
                join.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ch join " + channel.getName() + " [password]"));
            }
            join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, joinHover.create()));
            join.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);

            TextComponent leave = new TextComponent(" [LEAVE]");
            ComponentBuilder leaveHover = new ComponentBuilder(ChatColor.RED + "Click to leave " + channel.getColor() + channel.getName());
            leave.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ch leave " + channel.getName()));
            leave.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, leaveHover.create()));
            leave.setColor(net.md_5.bungee.api.ChatColor.RED);

            section.addExtra(channelView);

            section.addExtra(focus);
            if (chatter != null && !chatter.getChannels().contains(channel))
                section.addExtra(join);
            else
                section.addExtra(leave);

            if (chatter != null && chatter.canRemove(channel) == Chatter.Result.ALLOWED) {
                TextComponent remove = new TextComponent(" [REMOVE]");
                ComponentBuilder removeHover = new ComponentBuilder(ChatColor.RED + "Click to copy remove cmd to chat box");
                remove.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ch remove " + channel.getName()));
                remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, removeHover.create()));
                remove.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                section.addExtra(remove);
            }

            ((Player) sender).spigot().sendMessage(section);
        }

        return true;
    }
}
