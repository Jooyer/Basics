package cn.lvsong.lib.ui.mvp

import androidx.annotation.ColorRes
import cn.lvsong.lib.library.utils.DensityUtil
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
    private var mTransY: Int = DensityUtil.dp2pxRtInt(48)

    /**
     *
     * 是否 fitsSystemWindows, 即在顶部加入一个padding,默认不加入,默认是的
     *  0 --> 不加, 1 --> 加上
     */
    private var mNeedUseImmersive = 0


    /**
     * 1 --> 状态栏文本是黑色, 2 --> 状态栏文本是白色
     * 默认黑色
     */
    private var mDarkModel = 2

    /**
     * 设置全局状态栏颜色
     */
    @ColorRes
    private var mStatusBarColor = R.color.main_theme_color
    /**
     * 设置LoadingView背景色
     */
    @ColorRes
    private var mLoadingViewBackgroundColor = R.color.main_theme_color

    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            StatusConfig()
        }
    }

    /**
     * 当 Activity / Fragment 存在 Toolbar 时,需将StatusManager往下移动 Toolbar高度
     */
    fun setTranslateY(transY: Int): StatusConfig {
        mTransY = transY
        return this
    }

    fun getTranslateY() = mTransY

    /**
     * 设置状态栏颜色,默认是白色
     */
    fun setStatusBarColor(@ColorRes statusBarColor: Int): StatusConfig {
        mStatusBarColor = statusBarColor
        return this
    }

    fun getStatusBarColor(): Int {
        return mStatusBarColor
    }


    /**
     * 设置LoadingView背景色
     */
    fun setLoadingViewBackgroundColor(@ColorRes loadingViewBackgroundColor: Int): StatusConfig {
        mLoadingViewBackgroundColor = loadingViewBackgroundColor
        return this
    }

    fun getLoadingViewBackgroundColor(): Int {
        return mLoadingViewBackgroundColor
    }

    /**
     * 是否 fitsSystemWindows, 即在顶部加入一个padding,默认不加入
     *  0 --> 不加, 1 --> 加上
     */
    fun setNeedUseImmersive(needUseImmersive: Int): StatusConfig {
        mNeedUseImmersive = needUseImmersive
        return this
    }

    fun needUseImmersive() = mNeedUseImmersive


    /**
     * 1 --> 状态栏文本是黑色, 2 --> 状态栏文本是白色
     * 默认黑色
     */
    fun setDarkModel(darkModel:Int){
        mDarkModel = darkModel
    }

    fun  getDarkModel() = mDarkModel

}