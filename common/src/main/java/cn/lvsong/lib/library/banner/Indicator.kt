package cn.lvsong.lib.library.banner

import android.view.View
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout


/**
 * 参考 https://hub.fastgit.org/zguop/banner
 * Desc: 指示器统一接口
 * Author: Jooyer
 * Date: 2020-11-28
 * Time: 11:04
 */
interface Indicator {

    /**
     * 设置有多少个指示器
     */
    fun initIndicatorCount(pageCount: Int)

    /**
     * 获取当前指示器控件
     */
    fun getIndicatorView(): View

    /**
     * 获取指示器容器的布局参数, 可以通过这里自行设置指示器位置
     */
    fun getIndicatorViewLayoutParam(): ConstraintLayout.LayoutParams

    /**
     * @param offset  -->  当前位置在屏幕总滑动,占据滑动方向总宽度的比率, [0,1)
     * @param offsetPx -->  当前位置偏移距离
     */
    fun onPageScrolled(position: Int, offset: Float, @Px offsetPx: Int)

    /**
     * 滑动完成后下标位置
     * 注意: 此方法中无需调用 invalidate() / postInvalidate() 方法
     */
    fun onPageSelected(position: Int)

}