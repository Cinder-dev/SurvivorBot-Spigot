package com.fireblazerrr.survivorbot.spigot;

import com.fireblazerrr.survivorbot.SurvivorBot;
import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.utils.Announcement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private String joinFormat = SurvivorBot.getJoinFormat();
    private String quitFormat = SurvivorBot.getQuitFormat();
    private String achievementFormat = "%s has just earned the achievement [%s]";

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SurvivorBot.isDiscordJoinLeave()) {
            Chatter joined = SurvivorBot.getChatterManager().addChatter(event.getPlayer());

            new Announcement(event.getPlayer());

            String newMessage = joinFormat.replace("{prefix}", joined.getTeam() == null ? "" : joined.getTeam().getPrefix())
                    .replace("{player}", joined.getName())
                    .replace("{suffix}", joined.getTeam() == null ? "" : joined.getTeam().getSuffix());
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', newMessage));
            if (SurvivorBot.getInstance().isMaster()) {
                SurvivorBot.getInstance().getDJA().getTextChannelById(
                        SurvivorBot.getChannelManager().getDefaultChannel().getDiscordChannelLinkID())
                        .sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', newMessage))).queue();
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", SurvivorBot.getChannelManager().getDefaultChannel().getName());
            jsonObject.addProperty("channel", "join");
            jsonObject.addProperty("user", event.getPlayer().getName());
            jsonObject.addProperty("message", newMessage);
            SurvivorBot.getJedisPool().getResource().publish("survivor", jsonObject.toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (SurvivorBot.isDiscordJoinLeave()) {
            Chatter quitter = SurvivorBot.getChatterManager().getChatter(event.getPlayer());
            String newMessage = quitFormat.replace("{prefix}", quitter.getTeam() == null ? "" : quitter.getTeam().getPrefix())
                    .replace("{player}", quitter.getName())
                    .replace("{suffix}", quitter.getTeam() == null ? "" : quitter.getTeam().getSuffix());
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', newMessage));
            if (SurvivorBot.getInstance().isMaster()) {
                SurvivorBot.getInstance().getDJA().getTextChannelById(
                        SurvivorBot.getChannelManager().getDefaultChannel().getDiscordChannelLinkID())
                        .sendMessage(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', newMessage))).queue();
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", SurvivorBot.getChannelManager().getDefaultChannel().getName());
            jsonObject.addProperty("channel", "leave");
            jsonObject.addProperty("user", event.getPlayer().getName());
            jsonObject.addProperty("message", newMessage);
            SurvivorBot.getJedisPool().getResource().publish("survivor", jsonObject.toString());
            SurvivorBot.getChatterManager().removeChatter(event.getPlayer());
        } else {

        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        SurvivorBot.getChatterManager().getChatter(event.getPlayer()).refocus();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAchievement(PlayerAchievementAwardedEvent event) {

        String achievementName = "";

        switch (event.getAchievement()) {
            case OPEN_INVENTORY:
                achievementName = "Taking Inventory";
                break;
            case MINE_WOOD:
                achievementName = "Getting Wood";
                break;
            case BUILD_WORKBENCH:
                achievementName = "Benchmarking";
                break;
            case BUILD_PICKAXE:
                achievementName = "Time to Mine!";
                break;
            case BUILD_FURNACE:
                achievementName = "Hot Topic";
                break;
            case ACQUIRE_IRON:
                achievementName = "Acquire hardware";
                break;
            case BUILD_HOE:
                achievementName = "Time to Farm!";
                break;
            case MAKE_BREAD:
                achievementName = "Bake Bread";
                break;
            case BAKE_CAKE:
                achievementName = "The Lie";
                break;
            case BOOKCASE:
                achievementName = "Librarian";
                break;
            case BREED_COW:
                achievementName = "Repopulation";
                break;
            case BUILD_BETTER_PICKAXE:
                achievementName = "Getting an Upgrade";
                break;
            case COOK_FISH:
                achievementName = "Delicious Fish";
                break;
            case ON_A_RAIL:
                achievementName = "On A Rail";
                break;
            case BUILD_SWORD:
                achievementName = "Time to Strike!";
                break;
            case KILL_ENEMY:
                achievementName = "Monster Hunter";
                break;
            case KILL_COW:
                achievementName = "Cow Tipper";
                break;
            case FLY_PIG:
                achievementName = "When Pigs Fly";
                break;
            case SNIPE_SKELETON:
                achievementName = "Sniper Duel";
                break;
            case GET_DIAMONDS:
                achievementName = "DIAMONDS!";
                break;
            case NETHER_PORTAL:
                achievementName = "We Need to Go Deeper";
                break;
            case GHAST_RETURN:
                achievementName = "Return to Sender";
                break;
            case GET_BLAZE_ROD:
                achievementName = "Into Fire";
                break;
            case BREW_POTION:
                achievementName = "Local Brewery";
                break;
            case END_PORTAL:
                achievementName = "The End?";
                break;
            case THE_END:
                achievementName = "The End.";
                break;
            case ENCHANTMENTS:
                achievementName = "Enchanter";
                break;
            case OVERKILL:
                achievementName = "Overkill";
                break;
            case EXPLORE_ALL_BIOMES:
                achievementName = "Adventuring Time";
                break;
            case SPAWN_WITHER:
                achievementName = "The Beginning?";
                break;
            case KILL_WITHER:
                achievementName = "The Beginning.";
                break;
            case FULL_BEACON:
                achievementName = "Beaconator";
                break;
            case DIAMONDS_TO_YOU:
                achievementName = "Diamonds to you!";
                break;
            case OVERPOWERED:
                achievementName = "Overpowered";
                break;
        }

        if (SurvivorBot.getInstance().isMaster()) {
            SurvivorBot.getInstance().getDJA().getTextChannelById(
                    SurvivorBot.getChannelManager().getDefaultChannel().getDiscordChannelLinkID()
            ).sendMessage(
                    String.format(achievementFormat, event.getPlayer().getName(), achievementName)
            ).queue();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", SurvivorBot.getChannelManager().getDefaultChannel().getName());
        jsonObject.addProperty("channel", "achievement");
        jsonObject.addProperty("user", event.getPlayer().getName());
        jsonObject.addProperty("message", String.format(achievementFormat, event.getPlayer().getName(), achievementName));
        SurvivorBot.getJedisPool().getResource().publish("survivor", jsonObject.toString());
    }

}
