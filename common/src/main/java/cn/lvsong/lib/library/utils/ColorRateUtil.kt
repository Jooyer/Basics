package cn.lvsong.lib.library.utils

import android.graphics.Color

/**
 * Desc: 获取颜色比例
 * Author: Jooyer
 * Date: 2018-09-26
 * Time: 14:57
 */
object ColorRateUtil {

    //获取某一个百分比间的颜色,radio取值[0,1]
    fun getColor(startColor: Int, endColor: Int, radio: Float): Int {
        val redStart = Color.red(startColor)
        val blueStart = Color.blue(startColor)
        val greenStart = Color.green(startColor)
        val redEnd = Color.red(endColor)
        val blueEnd = Color.blue(endColor)
        val greenEnd = Color.green(endColor)

        val red = (redStart + ((redEnd - redStart) * radio + 0.5)).toInt()
        val greed = (greenStart + ((greenEnd - greenStart) * radio + 0.5)).toInt()
        val blue = (blueStart + ((blueEnd - blueStart) * radio + 0.5)).toInt()
        return Color.argb(255, red, greed, blue)
    }

}