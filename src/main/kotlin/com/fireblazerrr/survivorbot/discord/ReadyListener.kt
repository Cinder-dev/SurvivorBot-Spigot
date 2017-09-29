package com.fireblazerrr.survivorbot.discord

import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.logging.Logger

class ReadyListener : ListenerAdapter(){

    val logger: Logger = Logger.getLogger("Minecraft")

    override fun onReady(event: ReadyEvent) {
        logger.info("*** Discord Bot Ready ***")
        event.jda.guilds.forEach { guild: Guild ->
            logger.info("===== ${guild.name} [${guild.id}] =====")
            logger.info("///// Text Channels /////")
            guild.textChannels.forEach { textChannel: TextChannel -> logger.info("     ${textChannel.name} [${textChannel.id}]") }
            logger.info("///// Voice Channels /////")
            guild.voiceChannels.forEach { voiceChannel: VoiceChannel -> logger.info("     ${voiceChannel.name} [${voiceChannel.id}]") }
            logger.info("///// Roles /////")
            guild.roles.forEach { role: Role -> logger.info("     ${role.name} [${role.id}]")}
            logger.info("///// Users /////")
            guild.members.forEach { member: Member -> logger.info("     ${member.nickname}(${member.effectiveName} [${member.user.id}])") }
            logger.info("================================================================================")
        }
    }
}