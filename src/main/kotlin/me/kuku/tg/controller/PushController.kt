package me.kuku.tg.controller

import com.IceCreamQAQ.Yu.annotation.Action
import com.IceCreamQAQ.YuWeb.annotation.WebController
import me.kuku.pojo.Result
import me.kuku.pojo.ResultStatus
import me.kuku.tg.TgBot
import me.kuku.tg.entity.UserService
import me.kuku.utils.MyUtils
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.ByteArrayInputStream
import java.util.*
import javax.inject.Inject

@WebController
class PushController {

    @Inject
    private lateinit var userService: UserService
    @Inject
    private lateinit var tgBot: TgBot

    @Action("/md/{key}")
    fun md(key: String, msg: String?): Result<*> {
        if (Objects.isNull(msg)) return ResultStatus.PARAM_ERROR.toResult()
        val userEntity = userService.findByKey(key)
            ?: return ResultStatus.DATA_NOT_EXISTS.toResult()
        tgBot.silent().sendMd(msg, userEntity.userid)
        return Result.success()
    }

    @Action("/image/{key}")
    fun image(key: String, url: String?, base: String?): Result<*> {
        if (Objects.isNull(url) && Objects.isNull(base)) return ResultStatus.PARAM_ERROR.toResult()
        val userEntity = userService.findByKey(key)
            ?: return ResultStatus.DATA_NOT_EXISTS.toResult()
        val inputFile = if (!Objects.isNull(url)) InputFile(url)
        else {
            val bytes = try {
                Base64.getDecoder().decode(base)
            } catch (e: Exception) {
                return Result.failure("解析base64出现异常", null)
            }
            InputFile(ByteArrayInputStream(bytes), "${MyUtils.randomStr(5)}.jpg")
        }
        val sendPhoto = SendPhoto(userEntity.userid.toString(), inputFile)
        tgBot.execute(sendPhoto)
        return Result.success()
    }

}