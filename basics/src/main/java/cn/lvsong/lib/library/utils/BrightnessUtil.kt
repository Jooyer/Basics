package cn.lvsong.lib.library.utils

import android.app.Activity
import android.provider.Settings
import android.view.WindowManager

/** https://blog.csdn.net/mp624183768/article/details/77449919?utm_source=blogxgwz2
 * Desc: 屏幕亮度
 * Author: Jooyer
 * Date: 2018-12-07
 * Time: 14:16
 */
object BrightnessUtil {

    fun getBrightness(context: Activity): Int = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)

    // 设置系统亮度
    fun setBrightness(context: Activity) {
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, getBrightness(context))
    }

    // 亮度是介于0~255之间的int类型值
    fun setBrightness(context: Activity, brightness: Int) {
        val window = context.window
        val param = window.attributes
        if (-1 == brightness) {
            param.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            param.screenBrightness = if (brightness <= 0) 1 / 255F else brightness / 255F
        }
        window.attributes = param
    }

}