package cn.lvsong.lib.library.utils

import java.util.regex.Pattern

/**
 * Desc: 电话正则
 * Author: Jooyer
 * Date: 2018-11-10
 * Time: 14:42
 */
object MobileUtils {

     val regEx =
        "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$"
    val pattern = Pattern.compile(regEx)


    fun isMobileNumber(mobile: String): Boolean {
        return pattern.matcher(mobile).matches()
    }


}