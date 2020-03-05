package cn.lvsong.lib.library.refresh

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.lvsong.lib.library.R

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 17:34
 */
open class DefaultFooterView(context: Context) : LinearLayout(context), IFooterWrapper {

    private var tvHeaderTip: TextView? = null
    private var cvLoading: ChrysanthemumView? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.footer_default, this, false)
        cvLoading = view.findViewById<ChrysanthemumView>(R.id.cv_loading)
        tvHeaderTip = view.findViewById<TextView>(R.id.tv_tip)
        val params = LinearLayout.LayoutParams(-1, -2)
        params.gravity = Gravity.CENTER_VERTICAL
        addView(view, params)
    }

    override fun onMoveDistance(distance: Int) {

    }


    override fun onPullUp() {
        tvHeaderTip?.text = "上拉加载更多"
    }

    override fun onPullUpAndReleasable() {
        tvHeaderTip?.text = "松手可加载"
    }


    override fun onLoading() {
        cvLoading?.visibility = View.VISIBLE
        tvHeaderTip?.text = "正在加载"
    }

    override fun onLoadComplete(isLoadSuccess: Boolean) {
        cvLoading?.visibility = View.GONE
        tvHeaderTip?.text = "加载完成"
    }

    override fun onLoadFailure() {
        cvLoading?.visibility = View.GONE
        tvHeaderTip?.text = "加载失败"
    }

    override fun onNoMore() {
        cvLoading?.visibility = View.GONE
        tvHeaderTip?.text = "没有更多数据"
    }

    override fun getLoadHeight(): Int {
      return  dp2pxRtInt(50)
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