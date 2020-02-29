package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * 排除策略
 * https://blog.csdn.net/winter13292/article/details/51355046 (Gson 过滤字段的几种方法)
 * https://xinyo.org/archives/66207/
 * https://www.jianshu.com/p/a2f312c6e513
 * Desc: 排除请求某些字段不需要上传服务器却又上传了, 处理 {@link cn.lvsong.lib.net.cover.TypeBean#allExpose = 1 }情况
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 9:48
 */
class ExposeConverterFactory(private val gson: Gson) : Converter.Factory() {

    companion object {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        fun create(): ExposeConverterFactory {
            return ExposeConverterFactory(gson)
        }
    }

    /**
     * @param annotations  --> Service 请求方法参数的注解
     * @param methodAnnotations  --> Service 请求方法的注解
     */
    override fun requestBodyConverter(
        type: Type, annotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody>? {
//        println("requestBodyConverter===========${TypeToken.get(type).rawType}")
        var annotation: TypeData? = null
        var typeAnnotation: TypeBean? = null
        methodAnnotations.forEach {
            when (it) {
                is TypeData -> {
                    annotation = it
                }
                is TypeBean -> {
                    typeAnnotation = it
                }
            }
        }

        return if (null != annotation && null != typeAnnotation && 1 == typeAnnotation!!.allExpose) {
            val adapter = gson.getAdapter(TypeToken.get(type))
//            println("requestBodyConverter===========0000000000000")
            DataRequestBodyConverter(adapter, gson)
        } else {
//            println("requestBodyConverter===========1111111111111")
            super.requestBodyConverter(type, annotations, methodAnnotations, retrofit)
        }
    }

}