package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.R

/**
 * Desc: https://blog.csdn.net/smileiam/article/details/73603840
 * Author: Jooyer
 * Date: 2018-09-13
 * Time: 11:36
 */
object StatusBarUtil {

    /**
     * 设置状态栏为透明
     * @param fitsSystemWindows --> true, 则内容不延伸到 StatusBar 内部
     */
    fun transparentStatusBar(
        activity: AppCompatActivity,
        @ColorRes statusBarColor: Int,
        fitsSystemWindows: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // 状态栏
            window.statusBarColor = ContextCompat.getColor(activity, statusBarColor)
            // 虚拟导航键
//            window.setNavigationBarColor(Color.YELLOW);
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        val contentView = activity.findViewById(android.R.id.content) as ViewGroup
        if (fitsSystemWindows) {
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0)
        } else {
            contentView.setPadding(0, 0, 0, 0)
        }
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(activity: AppCompatActivity): Int {
        val statusBarHeightDimenId =
            activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return activity.resources.getDimensionPixelSize(statusBarHeightDimenId)
    }

    /**
     * 更改状态栏高度
     */
    fun changeStatusBarColor(activity: AppCompatActivity, @ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = ContextCompat.getColor(activity, colorRes)
        }
    }

    /**
     * 改变状态栏颜色
     * @param dark --> true表示黑暗模式,则状态栏文字为白色,反之黑色
     */
    fun changeStatusTextColor(activity: AppCompatActivity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    /**
     * 设置状态栏颜色
     */
    fun setStatusBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = color
        }
    }

    /**
     * 设置底部虚拟导航栏颜色
     */
    fun setNavigationBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }


    // https://www.jianshu.com/p/205b8f5adb48
    fun showStatusBar(activity: Activity) {
        val attrs = activity.window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        activity.window.attributes = attrs
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun hideStatusBar(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        decorView.systemUiVisibility = uiOptions
    }

    // https://www.cnblogs.com/muhuacat/p/7447484.html
    fun hideNavigationBar(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
    }

    fun showNavigationBar(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions
    }

    /**
     * 设置状态栏透明
     * https://blog.csdn.net/weixin_39947864/article/details/81906140 (详细 )
     * https://www.jb51.net/article/111936.htm
     *
     * @param activity
     * @param show
     */
    fun setStatusBarVisible(activity: Activity, show: Boolean) {
        if (show) {
            var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            uiFlags = uiFlags and 0x00001000
            activity.window.decorView.systemUiVisibility = uiFlags
        } else {
            var uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    * View.SYSTEM_UI_FLAG_FULLSCREEN)
            uiFlags = uiFlags and 0x00001000
            activity.window.decorView.systemUiVisibility = uiFlags
        }
    }

    /**
     * 显示隐藏状态栏，全屏不变，只在有全屏时有效
     *
     * @param enable --> true 表示显示状态栏文字
     */
    fun setStatusBarVisibility(activity: Activity, enable: Boolean) {
        setStatusBarColor(activity, ContextCompat.getColor(activity, android.R.color.white))
        val lp = activity.window.attributes
        if (enable) {
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
        } else {
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN.inv()
        }
        activity.window.attributes = lp
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }


}