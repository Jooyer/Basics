package cn.lvsong.lib.library.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import androidx.annotation.NonNull


/** 全面屏适配底部虚拟导航栏: https://www.jianshu.com/p/b20047fdea8a
 * Desc: https://www.cnblogs.com/ldq2016/p/6905429.html
 * Author: Jooyer
 * Date: 2018-09-27
 * Time: 22:02
 */
object ScreenUtils {

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return
     */
    @Deprecated("由于有反射,故放弃...")
    private fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }

        return hasNavigationBar
    }


    /**
     *
     * @return  true --> 存在 NavigationBar
     */
    private fun checkDeviceHasNavigationBar(@NonNull context: Activity): Boolean {
        val window = context.window
        val display = window.windowManager.defaultDisplay
        val point = Point()
        display.getRealSize(point)

        val decorView = window.decorView
        val conf = context.resources.configuration
        return if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) { // 横屏
            val contentView = decorView.findViewById<View>(android.R.id.content)
            point.x != contentView.width
        } else {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            rect.bottom != point.y
        }
    }


    /**
     * 获取虚拟功能键高度
     * @param context
     * @return
     */
    //获取虚拟按键的高度
    fun getNavigationBarHeight(context: Activity): Int {
        return if (isNavigationBarAvailable(context)) {
            val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            //获取NavigationBar的高度
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     *  获取显示的高度,不正确
     */
    fun getRealHeight(context: Activity): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outPoint = Point()
        display.getRealSize(outPoint)
        val navigationBarHeight = getNavigationBarHeight(context)
//        println("getRealHeight=========${isNavigationBarAvailable(context)}  --> navigationBarHeight: $navigationBarHeight")
        return outPoint.y - navigationBarHeight
    }

    /**
     * 真实屏幕的高度
     */
    fun getScreenHeight(context: Activity): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outPoint = Point()
        windowManager.defaultDisplay.getRealSize(outPoint)
        return outPoint.y
    }

    fun getRealWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val outPoint = Point()
        display.getRealSize(outPoint)
        return outPoint.x
    }

    /** 这个方法只能判断存在虚拟按键,无法判断是显示状态还是隐藏的
     * 是否有下方虚拟栏
     * @return true --> 有虚拟按键
     */
     fun isNavigationBarAvailable(context: Activity): Boolean {
        // 菜单键(不是虚拟键,是手机屏幕外的按键)
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        // 返回键(不是虚拟键,是手机屏幕外的按键)
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        // Home键(不是虚拟键,是手机屏幕外的按键)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
        return !(hasBackKey && hasHomeKey) || !(hasMenuKey && hasBackKey)
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     *
     * @return
     */
     fun getDeviceInfo(): String {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"
        return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
            "navigationbar_is_min"
        } else if (brand.equals("XIAOMI", ignoreCase = true)) {
            "force_fsg_nav_bar"
        } else if (brand.equals("VIVO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("OPPO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("samsung", ignoreCase = true)) {
            "navigationbar_hide_bar_enabled"
        } else {
            "navigationbar_is_min"
        }
    }

    /**
     *
     * @param context
     * @return 返回true表示显示虚拟导航键、false表示隐藏虚拟导航键
     */
    fun hasNavigationBar(context: Activity): Boolean {
        //navigationGestureEnabled()从设置中取不到值的话，返回false，因此也不会影响在其他手机上的判断
        return isNavigationBarAvailable(context) && !navigationGestureEnabled(context)
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     * 获取主流手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     * @param context Context
     * @return
     * false 表示使用的是虚拟导航键(NavigationBar)，
     * true 表示使用的是手势， 默认是false
     *
     * 全面屏（是否开启全面屏开关 0 关闭  1 开启）
     */
     fun navigationGestureEnabled(context: Context): Boolean {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand))
            return 0 != Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
        return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
            0 != Settings.System.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } else if (brand.equals("XIAOMI", ignoreCase = true)) {
            0 != Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0)
        } else if (brand.equals("VIVO", ignoreCase = true)) {
            0 != Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0)
        } else if (brand.equals("OPPO", ignoreCase = true)) {
            0 != Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0)
        } else if (brand.equals("samsung", ignoreCase = true)) {
            0 != Settings.Global.getInt(context.contentResolver, "navigationbar_hide_bar_enabled", 0)
        } else {
            0 != Settings.Global.getInt(context.contentResolver, "navigation_gesture_on", 0)
        }
    }

    /**
     * 判断设备是否存在NavigationBar
     * @return true 存在, false 不存在
     */
    @Deprecated("Android 9以后不能用了")
    private fun deviceHasNavigationBar(): Boolean {
        var haveNav = false
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            val windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal")
            val getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService")
            getWmServiceMethod.isAccessible = true
            //getWindowManagerService是静态方法，所以invoke null
            val iWindowManager = getWmServiceMethod.invoke(null)

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            val iWindowManagerClass = iWindowManager.javaClass
            val hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar")
            hasNavBarMethod.isAccessible = true
            haveNav = hasNavBarMethod.invoke(iWindowManager) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return haveNav
    }

}