package cn.lvsong.lib.library.refresh

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import cn.lvsong.lib.library.R

/**
 * Desc: 默认加载控件
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 17:34
 */
open class DefaultFooterView(context: Context) : LinearLayout(context), IFooterWrapper {

    private var tvHeaderTip: TextView? = null
    private var cvLoading: View? = null
    private var guideline: Guideline?=null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.footer_default, this, false)
        cvLoading = view.findViewById(R.id.cv_loading)
        tvHeaderTip = view.findViewById(R.id.tv_tip)
        guideline = view.findViewById(R.id.gl_center_line)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getLoadHeight()
        )
        params.gravity = Gravity.CENTER_VERTICAL
        addView(view, params)
    }

    override fun onMoveDistance(distance: Int) {

    }


    override fun onPullUp() {
        cvLoading?.visibility = View.VISIBLE
        guideline?.setGuidelinePercent(0.5F)
        tvHeaderTip?.text = "上拉加载更多"
    }

    override fun onPullUpAndReleasable() {
        tvHeaderTip?.text = "松手可加载"
    }


    override fun onLoading() {
        tvHeaderTip?.text = "正在加载"
    }

    override fun onLoadComplete(isLoadSuccess: Boolean) {
//        cvLoading?.visibility = View.GONE
        tvHeaderTip?.text = "加载完成"
    }

    override fun onLoadFailure() {
        cvLoading?.visibility = View.GONE
        guideline?.setGuidelinePercent(0.4F)
        tvHeaderTip?.text = "加载失败"
    }

    override fun onNoMore() {
        cvLoading?.visibility = View.GONE
        guideline?.setGuidelinePercent(0.4F)
        tvHeaderTip?.text = "没有更多数据"
    }

    override fun getLoadHeight(): Int {
        return dp2pxRtInt(70)
    }

    /**
     * dp -> px
     * @param dp
     * @return
     */
    fun dp2pxRtInt(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}