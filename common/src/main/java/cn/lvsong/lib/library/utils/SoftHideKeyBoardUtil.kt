package cn.lvsong.lib.library.utils

import android.R
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import cn.lvsong.lib.library.utils.DensityUtil.dp2pxRtInt

/** https://www.aliyun.com/jiaocheng/73760.html(判断键盘是否弹出....)
 * Desc:
 * Author: Jooyer
 * Date: 2018-11-06
 * Time: 17:54
 */
class SoftHideKeyBoardUtil private constructor(activity: Activity) {
    private val mChildOfContent: View
    private var usableHeightPrevious = 0
    private val frameLayoutParams //为适应华为小米等手机键盘上方出现黑条或不适配
            : FrameLayout.LayoutParams
    private var contentHeight //获取setContentView本来view的高度
            = 0
    private var isfirst = true //只用获取一次
    private val statusBarHeight = dp2pxRtInt(0f) // 通过这个参数,可以调整输入框位置

    companion object {
        fun assistActivity(activity: Activity) {
            SoftHideKeyBoardUtil(activity)
        }
    }

    init { //1､找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        val content =
            activity.findViewById<View>(R.id.content) as FrameLayout //2､获取到setContentView放进去的View
        mChildOfContent =
            content.getChildAt(0) //3､给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            if (isfirst) {
                contentHeight = mChildOfContent.height //兼容华为等机型
                isfirst = false
            } //5､当前布局发生变化时，对Activity的xml布局进行重绘
            possiblyResizeChildOfContent()
        } //6､获取到Activity的xml布局的放置参数
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    } // 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度

    private fun possiblyResizeChildOfContent() { //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        val usableHeightNow = computeUsableHeight() //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {  //3､获取Activity中xml中布局在当前界面显示的高度
            val usableHeightSansKeyboard =
                mChildOfContent.rootView.height //4､Activity中xml布局的高度-当前可用高度
            val heightDifference = usableHeightSansKeyboard - usableHeightNow //5､高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > usableHeightSansKeyboard / 4) {  // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                frameLayoutParams.height =
                    usableHeightSansKeyboard - heightDifference + statusBarHeight
            } else {
                frameLayoutParams.height = contentHeight
            } //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r) // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        return r.bottom - r.top
    }
}