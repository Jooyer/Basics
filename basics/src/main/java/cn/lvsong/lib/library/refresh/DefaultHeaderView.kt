package cn.lvsong.lib.library.refresh

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.lvsong.lib.library.R
import cn.lvsong.lib.library.utils.DensityUtils

/**
 * Desc:
 * Author: Jooyer
 * Date: 2019-08-06
 * Time: 17:34
 */
class DefaultHeaderView(context: Context) : LinearLayout(context), IHeaderWrapper {

    private var tvHeaderTip: TextView? = null
    private var ivHeaderTip: ImageView? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.header_default, this, false)
        ivHeaderTip = view.findViewById<ImageView>(R.id.iv_tip)
        tvHeaderTip = view.findViewById<TextView>(R.id.tv_tip)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER_VERTICAL
        setBackgroundColor(Color.WHITE)
        addView(view, params)
    }

    override fun onMoveDistance(distance: Int) {
        Log.e("DefaultHeaderView","onMoveDistance========distance: $distance")
    }


    override fun onPullDown() {
        tvHeaderTip?.text = "下拉刷新"
    }

    override fun onPullDownAndReleasable() {
        tvHeaderTip?.text = "松手可刷新"
    }

    override fun onRefreshReady() {

    }

    override fun onRefreshing() {
        tvHeaderTip?.text = "正在刷新"
    }

    override fun onRefreshComplete(isRefreshSuccess: Boolean) {
        tvHeaderTip?.text = "刷新完成"
    }

    override fun onRefreshCancel() {

    }

    override fun getRefreshHeight(): Int {
        return DensityUtils.dpToPx(60)
    }

}