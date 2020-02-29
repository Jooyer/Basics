package cn.lvsong.lib.net.response

import androidx.annotation.Keep

/**
 * Desc: 不需要返回值时 , 只需要成功与否的状态和提示文案
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 14:47
 */
@Keep
data class Response(val code: Int, val msg: String)