@file:Suppress("UNCHECKED_CAST")

package me.kuku.tg

import com.IceCreamQAQ.Yu.annotation.Config
import com.IceCreamQAQ.Yu.annotation.Event
import com.IceCreamQAQ.Yu.annotation.EventListener
import com.IceCreamQAQ.Yu.di.ClassContext
import com.IceCreamQAQ.Yu.di.YuContext
import com.IceCreamQAQ.Yu.event.events.AppStartEvent
import com.IceCreamQAQ.Yu.hook.HookItem
import com.IceCreamQAQ.Yu.hook.YuHook
import com.IceCreamQAQ.Yu.loader.AppClassloader
import com.IceCreamQAQ.Yu.module.Module
import com.icecreamqaq.yuq.YuQStarter
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

fun main() {
    val packageName = listOf("org.telegram.telegrambots",
        "me.kuku.tg.entity.UserEntity", "jdk")
    AppClassloader.registerBackList(packageName)
    YuHook.put(HookItem("org.hibernate.Version", "initVersion", "com.icecreamqaq.yudb.HibernateVersionHook"))
    YuQStarter.start()
}

@EventListener
class RegisterBot{

    @Inject
    private lateinit var context: YuContext
    @Inject
    private lateinit var tgBot: TgBot

    @Event
    fun tgStart(e: AppStartEvent?) {
        val field = context::class.java.getDeclaredField("classContextMap")
        field.isAccessible = true
        val map = field.get(context) as HashMap<String, ClassContext>
        map.forEach{(_,v) ->
            v.clazz.getAnnotation(Extension::class.java)?.let {
                var ob = v.defaultInstance as? AbilityExtension
                if (Objects.isNull(ob)) {
                    try {
//                        context[v.clazz]
                        val s = v.clazz.getDeclaredConstructor().newInstance()
                        context.putBean(s)
                        context.injectBean(s)
                        ob = s as? AbilityExtension
                    } catch (e: Exception) {
                    }
                }
                ob?.apply {
                    tgBot.addExtension(this)
                }
            }
        }
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(tgBot)
    }
}

class TgModule: Module {
    @Inject
    private lateinit var context: YuContext
    @Config("me.kuku.botToken")
    private lateinit var botToken: String
    @Config("me.kuku.botUsername")
    private lateinit var botUsername: String
    @Config("me.kuku.creatorId")
    private lateinit var creatorId: String

    override fun onLoad() {
        val tgBot = TgBot(botToken, botUsername, creatorId.toLong())
        context.putBean(tgBot, "tgBot")
    }
}

class TgBot(botToken: String, botUsername: String, private val creatorId: Long): AbilityBot(botToken, botUsername) {
    override fun creatorId(): Long = creatorId

    public override fun addExtension(extension: AbilityExtension) {
        super.addExtension(extension)
    }
}

annotation class Extension