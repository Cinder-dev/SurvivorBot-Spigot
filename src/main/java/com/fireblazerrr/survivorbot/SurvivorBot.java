package com.fireblazerrr.survivorbot;

import com.fireblazerrr.survivorbot.channel.Channel;
import com.fireblazerrr.survivorbot.channel.ChannelManager;
import com.fireblazerrr.survivorbot.channel.YMLChannelStorage;
import com.fireblazerrr.survivorbot.chatter.Chatter;
import com.fireblazerrr.survivorbot.chatter.ChatterManager;
import com.fireblazerrr.survivorbot.chatter.YMLChatterStorage;
import com.fireblazerrr.survivorbot.discord.Instance;
import com.fireblazerrr.survivorbot.jedis.JedisManager;
import com.fireblazerrr.survivorbot.spigot.command.CommandHandler;
import com.fireblazerrr.survivorbot.spigot.command.commands.*;
import com.fireblazerrr.survivorbot.spigot.listeners.PlayerListener;
import com.fireblazerrr.survivorbot.utils.ChatLogFormatter;
import com.fireblazerrr.survivorbot.utils.ConfigManager;
import com.fireblazerrr.survivorbot.utils.message.MessageHandler;
import com.fireblazerrr.survivorbot.utils.message.MessageNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SurvivorBot extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final Logger chatLog = Logger.getLogger("SurvivorBot");
    private static final CommandHandler commandHandler = new CommandHandler();
    private static final ChannelManager channelManager = new ChannelManager();
    private static final ChatterManager chatterManager = new ChatterManager();
    private static final MessageHandler messageHandler = new MessageHandler();
    private static final ConfigManager configManager = new ConfigManager();
    private static final MessageNotFoundException except = new MessageNotFoundException();
    private static final JedisManager jedisManager = new JedisManager();
    private static final boolean DEBUG = true;
    private static Instance instance = new Instance();
    private static SurvivorBot plugin;
    private static ResourceBundle messages;
    private static boolean chatLogEnabled;
    private static boolean logToBukkit;
    private Thread jedisManagerThread;

    public SurvivorBot() {
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static String getMessage(String key) throws MessageNotFoundException {
        String msg = messages.getString(key);
        if (msg == null) {
            throw except;
        } else {
            return msg;
        }
    }

    public static SurvivorBot getPlugin() {
        return plugin;
    }

    public static void logChat(String message) {
        if (chatLogEnabled) {
            chatLog.info(ChatColor.stripColor(message));
        }
    }

    public static void setChatLogEnabled(boolean chatLogEnabled) {
        SurvivorBot.chatLogEnabled = chatLogEnabled;
    }

    public static void setLogToBukkitEnabled(boolean enabled) {
        logToBukkit = enabled;
    }

    public static void setLocale(Locale locale) throws ClassNotFoundException {
        messages = ResourceBundle.getBundle("messages", locale);
        if (messages == null) {
            throw new ClassNotFoundException("messages");
        }
    }

    public static ChannelManager getChannelManager() {
        return channelManager;
    }

    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public static CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public static ChatterManager getChatterManager() {
        return chatterManager;
    }

    public static JedisManager getJedisManager() {
        return jedisManager;
    }

    public static Instance getInstance() {
        return instance;
    }

    public static void debug(String identifier, String... args) {
        if (DEBUG) {
            final String[] results = {""};
            Arrays.stream(args).forEach(s -> results[0] += s + " | ");
            log.info("[SurvivorBot Debug] " + identifier + " | " + results[0]);
        }
    }

    public static void info(String message) {
        log.info("[SurvivorBot] " + message);
    }

    public static void severe(String message) {
        log.severe("[SurvivorBot] " + message);
    }

    public static void warning(String message) {
        log.warning("[SurvivorBot] " + message);
    }

    public static boolean hasChannelPermission(Player player, Channel channel, Chatter.Permission permission) {
        String formedPermission = permission.form(channel).toLowerCase();
        return player.isPermissionSet(formedPermission) ? player.hasPermission(formedPermission) :
                player.hasPermission(permission.formAll());
    }

    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        return commandHandler.dispatch(sender, lable, args);
    }

    public void onDisable() {
        if (channelManager.getStorage() != null) {
            channelManager.getStorage().update();
            channelManager.clear();
        }
        if (chatterManager.getStorage() != null) {
            chatterManager.getStorage().update();
            chatterManager.clear();
        }

//        jedisManager.destroy();
//        if (instance.isMaster())
//            instance.terminate();

//        jedisManagerThread.stop();

        info("Version " + this.getDescription().getVersion() + " is disabled.");
    }

    public void onEnable() {
        plugin = this;
        this.setupStorage();
        channelManager.loadChannels();
        configManager.load(new File(this.getDataFolder(), "config.yml"));


        channelManager.getStorage().update();
        this.setupChatLog();
        Bukkit.getServer().getOnlinePlayers().forEach(chatterManager::addChatter);

        this.registerCommands();
        this.registerEvents();
        info("Version " + this.getDescription().getVersion() + " is enabled.");

        // Create Discord Bot Instance
        if (instance.isMaster()) {
            instance.setupInstance();
        }

        // Create Jedis Event Listener
//        jedisManagerThread = new Thread(jedisManager);
//        jedisManagerThread.start();

    }

    public void setupStorage() {
        File channelFolder = new File(this.getDataFolder(), "channels");
        channelFolder.mkdirs();
        YMLChannelStorage channelStorage = new YMLChannelStorage(channelFolder);
        channelManager.setStorage(channelStorage);
        File chatterFolder = new File(this.getDataFolder(), "chatters");
        chatterFolder.mkdirs();
        YMLChatterStorage chatterStorage = new YMLChatterStorage(chatterFolder);
        chatterManager.setStorage(chatterStorage);
    }

    private void registerCommands() {
        commandHandler.addCommand(new FocusCommand());
        commandHandler.addCommand(new JoinCommand());
        commandHandler.addCommand(new LeaveCommand());
        commandHandler.addCommand(new QuickMsgCommand());
        commandHandler.addCommand(new IgnoreCommand());
        commandHandler.addCommand(new MsgCommand());
        commandHandler.addCommand(new ReplyCommand());
        commandHandler.addCommand(new ListCommand());
        commandHandler.addCommand(new WhoCommand());
        commandHandler.addCommand(new AFKCommand());
        commandHandler.addCommand(new EmoteCommand());
        commandHandler.addCommand(new CreateCommand());
        commandHandler.addCommand(new RemoveCommand());
        commandHandler.addCommand(new SetCommand());
        commandHandler.addCommand(new InfoCommand());
        commandHandler.addCommand(new MuteCommand());
        commandHandler.addCommand(new KickCommand());
        commandHandler.addCommand(new BanCommand());
        commandHandler.addCommand(new ModCommand());
        commandHandler.addCommand(new SaveCommand());
        commandHandler.addCommand(new ReloadCommand());
        commandHandler.addCommand(new HelpCommand());
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        PlayerListener pcl = new PlayerListener(this);
        this.getCommand("ch").setTabCompleter(pcl);
        pm.registerEvents(pcl, this);
    }

    private void setupChatLog() {
        if (chatLogEnabled) {
            chatLog.setLevel(Level.INFO);
            chatLog.setParent(log);
            chatLog.setUseParentHandlers(logToBukkit);
            File logDir = new File(this.getDataFolder(), "logs");
            logDir.mkdirs();
            String fileName = logDir.getAbsolutePath() + "/chat.%g.%u.log";

            try {
                FileHandler e = new FileHandler(fileName, 524288, 1000, true);
                e.setFormatter(new ChatLogFormatter());
                chatLog.addHandler(e);
            } catch (IOException ex) {
                warning("Failed to create chat log handler.");
                ex.printStackTrace();
            }
        }
    }
}
