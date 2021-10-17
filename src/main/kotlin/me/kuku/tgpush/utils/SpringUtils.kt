package me.kuku.tgpush.utils

import me.kuku.tgpush.Extension
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringUtils: ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext
        }
    }

    companion object {
        private var applicationContext: ApplicationContext? = null

        // 获取applicationContext
        fun getApplicationContext(): ApplicationContext? {
            return applicationContext
        }

        // 通过name获取 Bean.
        fun getBean(name: String?): Any {
            return getApplicationContext()!!.getBean(name!!)
        }

        // 通过class获取Bean.
        fun <T> getBean(clazz: Class<T>): T {
            return getApplicationContext()!!.getBean(clazz)
        }

        // 通过name,以及Clazz返回指定的Bean
        fun <T> getBean(name: String?, clazz: Class<T>): T {
            getApplicationContext()!!.getBeansWithAnnotation(Extension::class.java)
            return getApplicationContext()!!.getBean(name!!, clazz)
        }

        fun getBeansWithAnnotation(annotationType: Class<out Annotation>): MutableMap<String, Any> {
            return getApplicationContext()!!.getBeansWithAnnotation(annotationType)
        }
    }
}