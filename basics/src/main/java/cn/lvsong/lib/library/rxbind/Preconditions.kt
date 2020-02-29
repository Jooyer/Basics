package cn.lvsong.lib.library.rxbind

import android.os.Looper

/**
 * Desc: 检测
 * Author: Jooyer
 * Date: 2018-09-21
 * Time: 15:23
 */
object Preconditions{

    fun <T> checkNotNull(value: T?, message: String): T {
        if (value == null) {
            throw NullPointerException(message)
        }
        return value
    }

    fun checkUiThread() {
        check(Looper.getMainLooper() == Looper.myLooper()) { "Must be called from the main thread. Was: " + Thread.currentThread() }
    }

}
