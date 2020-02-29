package cn.lvsong.lib.library.refresh

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
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
class DefaultFooterView(context: Context) : LinearLayout(context), IFooterWrapper {

    private var tvHeaderTip: TextView? = null
    private var ivHeaderTip: ImageView? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.footer_default, this, false)
        ivHeaderTip = view.findViewById<ImageView>(R.id.iv_tip)
        tvHeaderTip = view.findViewById<TextView>(R.id.tv_tip)
        val params = LinearLayout.LayoutParams(-1, -2)
        params.gravity = Gravity.CENTER_VERTICAL
        setBackgroundColor(Color.WHITE)
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

    override fun onLoadReady() {

    }

    override fun onLoading() {
        tvHeaderTip?.text = "正在加载"
    }

    override fun onLoadComplete(isLoadSuccess: Boolean) {
        tvHeaderTip?.text = "加载完成"
    }

    override fun onLoadCancel() {

    }

    override fun onNoMore() {
        tvHeaderTip?.text = "没有更多数据"
    }

    override fun getLoadHeight(): Int {
      return  DensityUtils.dpToPx(50)
    }

}