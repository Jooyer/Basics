package cn.lvsong.lib.ui.mvp

import androidx.annotation.ColorRes
import cn.lvsong.lib.library.utils.DensityUtils
import cn.lvsong.lib.ui.R

/**
 * Desc: StatusManager 配置信息
 * Author: Jooyer
 * Date: 2019-11-26
 * Time: 22:17
 */
class StatusConfig {
    /**
     * 当需要将根布局下移时,可以设置此值
     */
    private var mTransY: Int = DensityUtils.dpToPx(48)

    /**
     * 设置全局状态栏颜色
     */
    @ColorRes
    private var mStatusBarColor = R.color.main_theme_color

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            StatusConfig()
        }
    }

    /**
     * 当 Activity / Fragment 存在 Toolbar 时,需将StatusManager往下移动 Toolbar高度
     */
    fun setTranslateY(transY: Int){
        mTransY = transY
    }

    fun getTranslateY() = mTransY

    /**
     * 设置状态栏颜色,默认是白色
     */
    fun setStatusBarColor( @ColorRes statusBarColor:Int){
        mStatusBarColor = statusBarColor
    }

     fun getStatusBarColor():Int {
        return mStatusBarColor
    }
}