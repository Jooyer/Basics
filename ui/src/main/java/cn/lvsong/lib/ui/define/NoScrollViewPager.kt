package cn.lvsong.lib.ui.define

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Desc: 不能滑动的 ViewPager
 * Author: Jooyer
 * Date: 2018-08-16
 * Time: 19:09
 */
class NoScrollViewPager(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet) {

    private var isCanScroll = false

    private var isSmoothScroll = true


    fun setScrollAble(canScroll: Boolean) {
        isCanScroll = canScroll
    }

    fun setSmoothScroll(canSmoothScroll: Boolean) {
        isSmoothScroll = canSmoothScroll
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onTouchEvent(ev)
    }


    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item,isSmoothScroll)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, isSmoothScroll)
    }



}