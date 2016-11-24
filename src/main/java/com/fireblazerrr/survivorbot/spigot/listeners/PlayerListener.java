package com.fireblazerrr.survivorbot.spigot.listeners;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerListener implements Listener, TabCompleter {

    private final SurvivorBot plugin;

    private ArrayList<String> channelIdentifiers = new ArrayList<>();
    private ArrayList<String> commandIdentifiers = new ArrayList<>();

    public PlayerListener(SurvivorBot plugin) {
        this.plugin = plugin;
        SurvivorBot.getChannelManager().getChannels().forEach(channel -> {
            channelIdentifiers.add(channel.getName().toLowerCase());
            channelIdentifiers.add(channel.getNick().toLowerCase());
        });
        SurvivorBot.getCommandHandler().getCommands().forEach(command -> {
            Arrays.stream(command.getIdentifiers()).forEach(s -> {
                if (!s.equals("ch"))
                    if (s.startsWith("ch "))
                        commandIdentifiers.add(s.replace("ch ", ""));
            });
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String msg = event.getMessage();
        final String format = event.getFormat();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> SurvivorBot.getMessageHandler().handle(player, msg, format));
        event.setCancelled(true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            ArrayList<String> results = new ArrayList<>();

            if (args.length == 1) {
                channelIdentifiers.stream()
                        .filter(s -> s.startsWith(args[0]))
                        .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                        .forEach(results::add);
                commandIdentifiers.stream()
                        .filter(s -> s.startsWith(args[0]))
                        .forEach(results::add);
            } else {
                results.clear();
                switch (args[0]) {
                    case "afk":
                        break;
                    case "ban":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        } else {
                            Bukkit.getServer().getOnlinePlayers().stream()
                                    .map(o -> (Player) o)
                                    .filter(player -> SurvivorBot.getChannelManager().getChannel(args[1]).getMembers().contains(SurvivorBot.getChatterManager().getChatter(player)))
                                    .map(Player::getName)
                                    .forEach(results::add);
                        }
                        break;
                    case "create":
                        break;
                    case "help":
                        break;
                    case "ignore":
                        if (args.length == 2) {
                            Bukkit.getServer().getOnlinePlayers().stream()
                                    .map(o -> (Player) o)
                                    .map(Player::getName)
                                    .forEach(results::add);
                        }
                        break;
                    case "info":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                    case "join":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                    case "kick":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        } else {
                            Bukkit.getServer().getOnlinePlayers().stream()
                                    .map(o -> (Player) o)
                                    .filter(player -> SurvivorBot.getChannelManager().getChannel(args[1]).getMembers().contains(SurvivorBot.getChatterManager().getChatter(player)))
                                    .map(Player::getName)
                                    .forEach(results::add);
                        }
                        break;
                    case "leave":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                    case "list":
                        break;
                    case "mod":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        } else {
                            Bukkit.getServer().getOnlinePlayers().stream()
                                    .map(o -> (Player) o)
                                    .filter(player -> SurvivorBot.getChannelManager().getChannel(args[1]).getMembers().contains(SurvivorBot.getChatterManager().getChatter(player)))
                                    .map(Player::getName)
                                    .forEach(results::add);
                        }
                        break;
                    case "msg":
                        break;
                    case "mute":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        } else {
                            Bukkit.getServer().getOnlinePlayers().stream()
                                    .map(o -> (Player) o)
                                    .filter(player -> SurvivorBot.getChannelManager().getChannel(args[1]).getMembers().contains(SurvivorBot.getChatterManager().getChatter(player)))
                                    .map(Player::getName)
                                    .forEach(results::add);
                        }
                        break;
                    case "qm":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                    case "reload":
                        break;
                    case "remove":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                    case "reply":
                        break;
                    case "save":
                        break;
                    case "set":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        } else {
                            String[] options = new String[]{"nick", "format", "password", "distance", "color", "shortcut", "verbose", "crossworld"};
                            if (args.length == 3) {
                                Arrays.stream(options).
                                        filter(s -> s.startsWith(args[2]))
                                        .forEach(results::add);
                            }
                        }
                        break;
                    case "who":
                        if (args.length == 2) {
                            channelIdentifiers.stream()
                                    .filter(s -> s.startsWith(args[1]))
                                    .filter(s -> SurvivorBot.getChatterManager().getChatter((Player) sender).getChannels().contains(SurvivorBot.getChannelManager().getChannel(s)))
                                    .forEach(results::add);
                        }
                        break;
                }
            }

            return results;
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreproccess(PlayerCommandPreprocessEvent event) {
        String input = event.getMessage().substring(1);
        String[] args = input.split(" ");
        Channel channel = SurvivorBot.getChannelManager().getChannel(args[0]);
        if (channel != null && channel.isShortcutAllowed()) {
            event.setCancelled(true);
            SurvivorBot.getCommandHandler().dispatch(event.getPlayer(), "ch qm", args);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        SurvivorBot.getChatterManager().addChatter(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        SurvivorBot.getChatterManager().removeChatter(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        SurvivorBot.getChatterManager().getChatter(event.getPlayer()).refocus();
    }
}
