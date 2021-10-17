package me.kuku.tgpush.config

import com.alibaba.fastjson.serializer.SerializerFeature
import com.alibaba.fastjson.support.config.FastJsonConfig
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter

@Configuration
class Beans {

    @Bean
    fun fastJsonHttpMessageConverters(): HttpMessageConverters {
        //1.需要定义一个convert转换消息的对象;
        val fastJsonHttpMessageConverter = FastJsonHttpMessageConverter()
        //2:添加fastJson的配置信息;
        val fastJsonConfig = FastJsonConfig()
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect)
        //3处理中文乱码问题
        val fastMediaTypes: MutableList<MediaType> = ArrayList<MediaType>()
        fastMediaTypes.add(MediaType.APPLICATION_JSON)
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.supportedMediaTypes = fastMediaTypes
        fastJsonHttpMessageConverter.fastJsonConfig = fastJsonConfig
        val converter: HttpMessageConverter<*> = fastJsonHttpMessageConverter
        return HttpMessageConverters(converter)
    }
}