package cn.lvsong.lib.demo.data

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * Desc: 数据返回公共部分的封装
 * Author: Jooyer
 * Date: 2020-09-02
 * Time: 10:27
 */

@Keep
@JsonClass(generateAdapter = true)
data class Response<T>(
    // 请求的数据内容, Object/List
    val data: T?,
    // 请求状态码
    val errorCode: Int,
    // 提示信息
    val errorMsg: String=""
)