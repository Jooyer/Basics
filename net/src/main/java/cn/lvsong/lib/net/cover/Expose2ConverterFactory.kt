package cn.lvsong.lib.net.cover

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * 排除策略
 * https://blog.csdn.net/winter13292/article/details/51355046 (Gson 过滤字段的几种方法)
 * https://xinyo.org/archives/66207/
 *
 * https://www.jianshu.com/p/a2f312c6e513
 * Desc: 排除服务器返回的数据,, 处理 {@link cn.lvsong.lib.net.cover.TypeBean#allExpose = 2 }情况
 * Author: Jooyer
 * Date: 2018-07-30
 * Time: 9:48
 */
class Expose2ConverterFactory(private val gson: Gson) : Converter.Factory() {

    companion object {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        fun create(): Expose2ConverterFactory {
            return Expose2ConverterFactory(gson)
        }
    }

    // //响应
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
//        println("responseBodyConverter======2=====${TypeToken.get(type).rawType}")
        var annotation: TypeData? = null
        var typeAnnotation: TypeBean? = null
        annotations.forEach {
            when (it) {
                is TypeData -> {
                    annotation = it
                }
                is TypeBean -> {
                    typeAnnotation = it
                }
            }
        }
        return if (null != annotation && null != typeAnnotation && 2 == typeAnnotation!!.allExpose) {
            val adapter = gson.getAdapter(TypeToken.get(type))
            DataResponseBodyConverter(adapter, gson, annotation!!)
        } else {
            super.responseBodyConverter(type, annotations, retrofit)
        }
    }

}