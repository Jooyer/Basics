package cn.lvsong.lib.net.utils

/**
 * Desc:各种回调
 * Author: Jooyer
 * Date: 2018-07-31
 * Time: 11:49
 */
interface CallBack<T> {
    fun callback(data: T)

    fun callError(code: Int = 0, msg: String) {

    }

    fun callNetError() {

    }


}