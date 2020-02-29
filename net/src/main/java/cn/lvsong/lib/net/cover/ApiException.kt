package cn.lvsong.lib.net.cover

import androidx.annotation.Keep

/**
 * Desc: 错误信息封装
 * Author: Jooyer
 * Date: 2019-06-28
 * Time: 12:39
 */
@Keep
class ApiException(val code: Int,val msg: String) : RuntimeException(msg) {
}