package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * https://www.cnblogs.com/baiqiantao/p/7512336.html  --> 高级用法
 * https://blog.csdn.net/qq_22804827/article/details/61915760
 * https://gitee.com/yang_share/codes/hckdsjf0ur5e96xy23lzi14
 * Desc: 自定义工厂方法解析注解的数据
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 9:48
 */

class DataConverterFactory(private val gson: Gson) : Converter.Factory() {

    companion object {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
            .create()

        fun create(): DataConverterFactory {
            return DataConverterFactory(gson)
        }
    }

    // //响应
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
//        println("responseBodyConverter=====Data======${TypeToken.get(type).rawType}")
        var annotation: TypeData? = null
        annotations.forEach {
            when (it) {
                is TypeData -> {
                    annotation = it
                }
            }
        }
        return when (annotation) {
            null -> super.responseBodyConverter(type, annotations, retrofit)
            else -> DataResponseBodyConverter(adapter, gson, annotation!!)
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
//        println("requestBodyConverter=====Data======${TypeToken.get(type).rawType}")
        var annotation: TypeData? = null
        methodAnnotations.forEach {
            when (it) {
                is TypeData -> {
                    annotation = it
                }
            }
        }

        return when (annotation) {
            null -> super.requestBodyConverter(type, annotations, methodAnnotations, retrofit)
            else -> {
                val adapter = gson.getAdapter(TypeToken.get(type))
                DataRequestBodyConverter(adapter, gson)
            }
        }
    }

}