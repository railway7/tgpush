package me.kuku.tgpush

import me.kuku.tgpush.utils.SpringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import javax.annotation.PostConstruct

@SpringBootApplication
class TgPushApplication

fun main(args: Array<String>) {
    runApplication<TgPushApplication>(*args)
}

@Service
class TgBot(@Value("\${me.kuku.botToken}") botToken: String, @Value("\${me.kuku.botUsername}") botUsername: String,
            @Value("\${me.kuku.creatorId}") private val creatorId: Long): AbilityBot(botToken, botUsername) {

    override fun creatorId(): Long = creatorId

    public override fun addExtension(extension: AbilityExtension) {
        super.addExtension(extension)
    }
}

@Component
class RegisterBot(private val tgBot: TgBot): CommandLineRunner {

    override fun run(vararg args: String?) {
        val list = SpringUtils.getBeansWithAnnotation(Extension::class.java)
        list.forEach{(_, v) ->
            try {
                val a = v as AbilityExtension
                tgBot.addExtension(a)
            } catch (e: Exception) {
            }
        }
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(tgBot)
    }
}

annotation class Extension