package cn.lvsong.lib.library.utils

import android.view.View

/**
 *
 * @ProjectName:    android
 * @ClassName:      Click
 * @Description:   各种点击拓展
 * @Author:         Jooyer
 * @CreateDate:     2020/6/2 13:52
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version:        1.0
 * https://www.jianshu.com/p/7118226ecba9
 */


/*

    基本用法
        View.withTrigger().click { // 礼物弹框

        }

 */


/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (View) -> Unit) = setOnClickListener {
    setOnClickListener {
        if (clickEnable()) {
            block(it as View)
        }
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (View) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as View)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else -601
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 600
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

/***
 * 带延迟过滤的点击事件监听，见[View.OnClickListener]
 * 延迟时间根据triggerDelay获取：600毫秒，不能动态设置
 */
interface OnLazyClickListener : View.OnClickListener {

    override fun onClick(view: View) {
        if (view.clickEnable()) {
            onTriggerClick(view)
        }
    }

    fun onTriggerClick(view: View)
}