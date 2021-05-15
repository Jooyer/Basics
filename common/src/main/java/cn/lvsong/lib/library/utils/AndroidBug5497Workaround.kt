package cn.lvsong.lib.library.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import cn.lvsong.lib.library.R

/** 如果想实现登录时 Logo 随键盘弹起和关闭缩小和方法,可以参考魔掠小说的 登录界面
 * Desc:  https://juejin.im/entry/58da15720ce463005716a123
 * Author: Jooyer
 * Date: 2018-09-26
 * Time: 16:42
 */
class AndroidBug5497Workaround private constructor(val activity: Activity) {

    private val mChildOfContent: View
    private var usableHeightPrevious = 0
    private val frameLayoutParams: FrameLayout.LayoutParams

    companion object {
        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
        fun assistActivity(activity: Activity) {
            AndroidBug5497Workaround(activity)
        }
    }

    init {
        val content = activity.findViewById<View>(R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }

}