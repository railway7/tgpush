package me.kuku.tg.controller

import me.kuku.tg.Extension
import me.kuku.tg.entity.UserEntity
import me.kuku.tg.entity.UserService
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.util.AbilityExtension
import java.util.*
import javax.inject.Inject

@Extension
class UserController: AbilityExtension {

    @Inject
    private lateinit var userService: UserService

    private fun key(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    fun newKey(): Ability = Ability
        .builder()
        .name("new")
        .info("create a key")
        .input(0)
        .locality(Locality.ALL)
        .privacy(Privacy.PUBLIC)
        .action {
            val id = it.user().id
            val userEntity = userService.findByUserid(id)
            val silent = it.bot().silent()
            if (!Objects.isNull(userEntity)) {
                silent.sendMd("You have already generated the key, if you need to regenerate it, you can use the /regen command",
                    it.chatId())
            } else {
                val key = key()
                userService.save(UserEntity(null, key, id))
                silent.sendMd("""
                    Success!
                    You key is `${key}`
                """.trimIndent(), it.chatId())
            }
        }
        .build()

    fun regen(): Ability = Ability
        .builder()
        .name("regen")
        .info("regen a key")
        .input(0)
        .locality(Locality.ALL)
        .privacy(Privacy.PUBLIC)
        .action {
            val id = it.user().id
            val userEntity = userService.findByUserid(id)
            val silent = it.bot().silent()
            if (userEntity == null) {
                silent.sendMd("You have not generated a key, if you want to generate a key, please use the /new command",
                    it.chatId())
            }else {
                val newKey = key()
                userEntity.key = newKey
                userService.save(userEntity)
                silent.sendMd("""
                    Success!
                    You new key is `${newKey}`
                """.trimIndent(), it.chatId())
            }
        }
        .build()

    fun query(): Ability = Ability
        .builder()
        .name("query")
        .info("query my key")
        .input(0)
        .locality(Locality.ALL)
        .privacy(Privacy.PUBLIC)
        .action {
            val id = it.user().id
            val userEntity = this.userService.findByUserid(id)
            val silent = it.bot().silent()
            if (userEntity == null) {
                silent.sendMd("You have not generated a key, if you want to generate a key, please use the /new command",
                    it.chatId())
            }else {
                val key = userEntity.key
                silent.sendMd("""
                    Success!
                    You key is `${key}`
                """.trimIndent(), it.chatId())
            }
        }
        .build()

}