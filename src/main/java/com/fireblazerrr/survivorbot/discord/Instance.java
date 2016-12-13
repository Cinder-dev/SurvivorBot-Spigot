package com.fireblazerrr.survivorbot.discord;

import com.fireblazerrr.survivorbot.SurvivorBot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Instance {

    private final AtomicBoolean reconnect = new AtomicBoolean(true);
    private boolean master = true;
    private String token = "";
    private String adminRankID = "";
    private String serverID = "";
    private String inviteURL = "";
    private boolean useAnnouncement = true;
    private String announcement = "Needs Updating!";
    private String announcementHeader = "&9===== &bRecent Announcement &9=====&f";
    private String announcementFooter = "&9=============================&f";
    private String announcementChannelID = "";
    private JDA jda;

    public Instance() {
    }

    public void setupInstance() {
        if (!token.equals("")) {
            try {
                SurvivorBot.info(token);
                jda = new JDABuilder(AccountType.BOT)
                        .setToken(token)
                        .addListener(new ReadyListener())
                        .addListener(new MessageListener())
                        .buildBlocking();
            } catch (LoginException | InterruptedException | RateLimitedException e) {
                e.printStackTrace();
            }
        } else {
            SurvivorBot.severe("Token is required for bot use");
        }
    }

//    public void terminate() {
//        reconnect.set(false);
//        try {
//            jda.logout();
//        } catch (DiscordException | RateLimitException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdminRankID() {
        return adminRankID;
    }

    public void setAdminRankID(String adminRankID) {
        this.adminRankID = adminRankID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public JDA getDJA() {
        return jda;
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public String getInviteURL() {
        return inviteURL;
    }

    public void setInviteURL(String inviteURL) {
        this.inviteURL = inviteURL;
    }

    public String getAnnouncementChannelID() {
        return announcementChannelID;
    }

    public void setAnnouncementChannelID(String announcementChannelID) {
        this.announcementChannelID = announcementChannelID;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getAnnouncementHeader() {
        return announcementHeader;
    }

    public void setAnnouncementHeader(String announcementHeader) {
        this.announcementHeader = announcementHeader;
    }

    public String getAnnouncementFooter() {
        return announcementFooter;
    }

    public void setAnnouncementFooter(String announcementFooter) {
        this.announcementFooter = announcementFooter;
    }

    public boolean isUseAnnouncement() {
        return useAnnouncement;
    }

    public void setUseAnnouncement(boolean useAnnouncement) {
        this.useAnnouncement = useAnnouncement;
    }
}
