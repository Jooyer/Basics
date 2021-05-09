package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


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
        activity: Activity,
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
     * 更改状态栏颜色
     */
    fun changeStatusBarColor(activity: AppCompatActivity, @ColorRes colorRes: Int) {
        changeStatusBarColorII(activity, ContextCompat.getColor(activity, colorRes))
    }

    /**
     * 更改状态栏颜色
     */
    fun changeStatusBarColorII(activity: Activity, @ColorInt colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = colorId
        }
    }

    /**
     * 改变状态栏颜色
     * @param dark --> true表示黑暗模式,则状态栏文字为白色,反之黑色
     */
    fun changeStatusTextColor(activity: Activity, dark: Boolean) {
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
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取 NavigationBar 的高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier(
            "navigation_bar_height",
            "dimen", "android"
        )
        return context.resources.getDimensionPixelSize(resourceId)
    }


    /**
     * 显示状态栏
     */
    fun showStatusBar(activity: Activity) {
        val decorView = activity.window.decorView
        val option =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        decorView.systemUiVisibility = option
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar(activity: Activity) {
        val decorView = activity.window.decorView
        val option =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = option
    }


    /**
     * https://www.cnblogs.com/muhuacat/p/7447484.html
     *  隐藏底部虚拟导航键
     */
    fun hideNavigationBar(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
    }

    /**
     *  显示底部虚拟导航键
     */
    fun showNavigationBar(activity: Activity) {
        val decorView = activity.window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions
    }

    /**
     * 设置状态栏透明,官网方法
     * https://developer.android.com/training/system-ui/status?hl=zh-cn
     * @param activity
     */
    fun setStatusBarTransparent(activity: Activity) {
        val decorView: View = activity.window.decorView
        // Hide the status bar
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        activity.window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
    }

    /**
     * 显示/隐藏状态栏，全屏不变，只在有全屏时有效
     *    requestWindowFeature(Window.FEATURE_NO_TITLE), window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
     *    在 setContentView() 之前调用上面方法设置全屏
     * @param enable --> true 表示显示状态栏, false 不显示状态栏
     * @param statusColor --> 状态栏颜色,默认白色
     */
    fun setStatusBarVisibility(
        activity: Activity,
        enable: Boolean,
        @ColorRes statusColor: Int = android.R.color.white
    ) {
        setStatusBarColor(activity, ContextCompat.getColor(activity, statusColor))
        val lp = activity.window.attributes
        if (enable) {
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
        } else {
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN.inv()
        }
        activity.window.attributes = lp
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////// 计算 StatusBar 和 NavigationBar 的高度 , 使用时将其拷贝到需要的地方 ////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** 来自:https://www.jianshu.com/p/9bfa5a30087c
     *  计算 StatusBar 和 NavigationBar 的高度 , 使用时将其拷贝到需要的地方
     *  请勿在dialog中使用
     *  主题的 android:windowTranslucentStatus 属性, 会影响 contentView 的 padding top.
     *  如果设置了 View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN , 那么 contentView 的 padding top 都是 0
     */
    private fun calStatusBarOrNavigationBarHeight(window: Window) {
        val decorView = window.decorView
        val measuredHeight = decorView.measuredHeight
        if (measuredHeight <= 0) {
            decorView.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    decorView.viewTreeObserver.removeOnPreDrawListener(this)
                    calStatusBarOrNavigationBarHeight(window)
                    return true
                }
            })
        } else {
            val outRect = Rect()
            decorView.getWindowVisibleDisplayFrame(outRect)
//            Log.i("StatusBar", "可视区域======:$outRect")
//            Log.i("StatusBar", "屏幕高度======:$measuredHeight")
            if (decorView is ViewGroup) {
                val childCount = decorView.childCount
//                if (childCount > 0) {
//                    val contentView =
//                        decorView.getChildAt(0)
//                }
                if (childCount > 1) {
                    val childView = decorView.getChildAt(1)
                    when {
                        isStatusBar(decorView, childView) -> {
                            Log.i("StatusBar", "状态栏高度:" + childView.measuredHeight)
                        }
                        isNavigationBar(decorView, childView) -> {
                            Log.i("StatusBar", "导航栏高度:" + childView.measuredHeight)
                        }
                        else -> {
                            Log.i("StatusBar", "未知:$childView")
                        }
                    }
                }
                if (childCount > 2) {
                    val childView = decorView.getChildAt(2)
                    when {
                        isStatusBar(decorView, childView) -> {
                            Log.i("StatusBar", "状态栏高度:" + childView.measuredHeight)
                        }
                        isNavigationBar(decorView, childView) -> {
                            Log.i("StatusBar", "导航栏高度:" + childView.measuredHeight)
                        }
                        else -> {
                            Log.i("StatusBar", "未知:$childView")
                        }
                    }
                }
            }
        }
    }

    private fun isStatusBar(
        decorView: View,
        childView: View
    ): Boolean {
        return childView.top == 0 && childView.measuredWidth == decorView.measuredWidth && childView.bottom < decorView.bottom
    }

    private fun isNavigationBar(
        decorView: View,
        childView: View
    ): Boolean {
        return childView.top > decorView.top && childView.measuredWidth == decorView.measuredWidth && childView.bottom == decorView.bottom
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////// 计算 StatusBar 和 NavigationBar 的高度 , 使用时将其拷贝到需要的地方 ////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}