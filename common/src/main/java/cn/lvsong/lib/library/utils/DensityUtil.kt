package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * 全面屏适配底部虚拟导航栏: https://www.jianshu.com/p/b20047fdea8a , https://www.cnblogs.com/ldq2016/p/6905429.html
 * Desc:  px与dp之间的转化
 * Created by Jooyer on 2016/4/18
 */
object DensityUtil {

    fun dp2pxRtInt(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun dp2pxRtFloat(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

    fun dp2pxRtInt(value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun dp2pxRtFloat(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

    fun sp2pxRtFloat(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

    fun sp2pxRtFloat(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    /**
     * 获取屏幕真实宽度,可用的不一定也是这么多
     */
    fun getRealWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outPoint = Point()
        display.getRealSize(outPoint)
        return outPoint.x
    }

    /**
     * 获取真实屏幕高度,可用的不一定也是这么多
     */
    fun getScreenHeight(context: Activity): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outPoint = Point()
        windowManager.defaultDisplay.getRealSize(outPoint)
        return outPoint.y
    }


}