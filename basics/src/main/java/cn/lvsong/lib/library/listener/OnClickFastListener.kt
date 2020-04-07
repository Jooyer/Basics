package cn.lvsong.lib.library.listener

import android.view.View

/** https://blog.csdn.net/qq_26971803/article/details/51284982
 * Desc: 解决快速点击问题(防抖动)
 * Author: Jooyer
 * Date: 2018-08-19
 * Time: 22:30
 */
abstract class OnClickFastListener(private val delay:Long = 900) : View.OnClickListener {
    // 防止快速点击默认等待时长为900ms
    private var DELAY_TIME: Long = delay
    private var lastClickTime: Long = 0

    private fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD in 1 until DELAY_TIME) {
            return true
        }
        lastClickTime = time
        return false
    }


    override fun onClick(v: View) {
        // 判断当前点击事件与前一次点击事件时间间隔是否小于阙值
        if (isFastDoubleClick()) {
            return
        }

        onFastClick(v)
    }

    /**
     * 设置默认快速点击事件时间间隔
     * @param delay_time
     * @return
     */
    fun setLastClickTime(delay_time: Long): OnClickFastListener {

        this.DELAY_TIME = delay_time

        return this
    }

    /**
     * 快速点击事件回调方法
     * @param v
     */
    abstract fun onFastClick(v: View)

}