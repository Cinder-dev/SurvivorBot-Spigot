package com.fireblazerrr.survivorbot.utils;

import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ChatLogFormatter extends Formatter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public ChatLogFormatter() {
    }

    public String format(LogRecord record) {
        return this.calcDate(record.getMillis()) + " " + ChatColor.stripColor(record.getMessage()) + "\n";
    }

    private String calcDate(long milli) {
        Date date = new Date(milli);
        return DATE_FORMAT.format(date);
    }
}
