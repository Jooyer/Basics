package cn.lvsong.lib.net.cover

import androidx.annotation.Keep

/**
 * Desc: 当提交服务器时使用 @Body,即上传对象时使用此注解,
 * 不使用自定义DataConverterFactory进行处理 , 使用 cn.lvsong.lib.net.cover.ExposeConverterFactory 排除不必要上传服务器的字段
 * 返回时在处理数据时也可以排除部分 ,使用 cn.lvsong.lib.net.cover.Expose2ConverterFactory / cn.lvsong.lib.net.cover.Expose3ConverterFactory
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 10:28
 */
@Keep
@Target(AnnotationTarget.FUNCTION)
annotation class TypeBean (
    /**
     * 默认 1 --> 表示只排除提交数据时使用, 2 --> 表示返回数据过滤, 3 --> 表示提交和返回都过滤
     */
    val allExpose : Int = 1
)
