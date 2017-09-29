package com.fireblazerrr.survivorbot.discord

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import java.util.logging.Logger

class DiscordBot {

    private val logger: Logger = Logger.getLogger("Minecraft")

    var token: String = ""
    private var jda: JDA? = null

    var master: Boolean = true
    var adminRankID: String = ""
    var serverID: String = ""
    var inviteURL: String = ""
    var useAnnouncement: Boolean = true
    var announcement: String = "Needs Updating!"
    var announcementHeader: String = "&9===== &bRecent Announcement &9=====&f"
    var announcementFooter: String = "&9=============================&f"
    var announcementChannelID: String = ""

    fun start() {
        if (token != "") {
            try {
                logger.info("Discord Bot Starting")
                jda = JDABuilder(AccountType.BOT)
                        .setToken(token)
                        .addEventListener(ReadyListener())
                        .addEventListener(MessageListener())
                        .buildBlocking()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } else
            logger.severe("Token is required for Discord cross chat.")
    }
}