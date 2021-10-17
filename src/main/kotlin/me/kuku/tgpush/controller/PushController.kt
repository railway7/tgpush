package me.kuku.tgpush.controller

import me.kuku.pojo.ResultStatus
import me.kuku.pojo.Result
import me.kuku.tgpush.TgBot
import me.kuku.tgpush.entity.UserService
import me.kuku.utils.MyUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.ByteArrayInputStream
import java.util.*
import javax.annotation.Resource

@RestController
class PushController {

    @Resource
    private lateinit var userService: UserService
    @Resource
    private lateinit var tgBot: TgBot

    @RequestMapping(value = ["/md/{key}"], method = [RequestMethod.GET, RequestMethod.POST])
    fun md(@PathVariable key: String, msg: String?): Result<*> {
        if (Objects.isNull(msg)) return ResultStatus.PARAM_ERROR.toResult()
        val userEntity = userService.findByKey(key)
            ?: return ResultStatus.DATA_NOT_EXISTS.toResult()
        tgBot.silent().sendMd(msg, userEntity.userid)
        return Result.success()
    }

    @RequestMapping(value = ["/image/{key}"], method = [RequestMethod.GET, RequestMethod.POST])
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