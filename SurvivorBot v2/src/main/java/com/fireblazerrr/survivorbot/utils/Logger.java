package com.fireblazerrr.survivorbot.utils;

import java.util.Arrays;

public class Logger {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");
    private static final java.util.logging.Logger chatLog = java.util.logging.Logger.getLogger("SurvivorBot");

    private final Class obj;

    public Logger(Class obj) {
        this.obj = obj;
    }

    public void info(String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(obj.getName()).append("] ");
        Arrays.stream(args).forEach(s -> sb.append(s).append(" "));
        log.info(sb.toString());
    }

    public void severe(String... args){
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(obj.getName()).append("] ");
        Arrays.stream(args).forEach(s -> sb.append(s).append(" "));
        log.severe(sb.toString());
    }

    public void warning(String... args){
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(obj.getName()).append("] ");
        Arrays.stream(args).forEach(s -> sb.append(s).append(" "));
        log.warning(sb.toString());
    }
}
