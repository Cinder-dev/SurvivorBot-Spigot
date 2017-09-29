package com.fireblazerrr.survivorbot.discord

import club.minnced.kjda.*
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class DiscordBot {

    private val logger: Logger = Logger.getLogger("Minecraft")

    var token: String = ""
    lateinit var jda: JDA

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
            logger.info("Discord Bot Starting")
            jda = client(AccountType.BOT){
                token { token }
                game { "survivorserver.com" }
                status { OnlineStatus.ONLINE }
                this += ReadyListener()
                this += MessageListener()

                httpSettings {
                    connectTimeout(2, TimeUnit.SECONDS)
                    readTimeout(3, TimeUnit.SECONDS)
                    writeTimeout(2, TimeUnit.SECONDS)
                }

                websocketSettings {
                    connectionTimeout = TimeUnit.SECONDS.toMillis(1).toInt()
                }
            }
        } else
            logger.severe("Token is required for Discord cross chat.")
    }
}