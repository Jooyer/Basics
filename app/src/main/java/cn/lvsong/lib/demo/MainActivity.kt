package cn.lvsong.lib.demo

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.SelectorFactory
import cn.lvsong.lib.ui.mvp.BaseActivity
import cn.lvsong.lib.ui.mvp.BasePresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<BasePresenter<*, *>>() {

    override fun getLayoutId() = R.layout.activity_main

    /**
     *  可以自行测试  1/0 效果
     */
    override fun needUseImmersive() = 1

    override fun getStatusBarColor() = R.color.main_theme_color

    override fun setLogic() {

    }

    override fun bindEvent() {
        btn_1.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()
    }

    /**
     * 普通刷新加载
     */
    fun onNormalRefresh(view: View) {
        startActivity(Intent(this,NormalRefreshActivity::class.java))
    }

    /**
     * NestedScroll嵌套滑动刷新加载
     */
    fun onNestedScrollRefresh(view: View) {
        startActivity(Intent(this,NestedScrollRefreshActivity::class.java))
    }

    /**
     * 懒加载
     */
    fun onLazyLoad(view: View) {
        startActivity(Intent(this,LazyLoadActivity::class.java))
    }

    /**
     * 使用StatusManager
     */
    fun onUserStatusManager(view: View) {
        startActivity(Intent(this,StatusActivity::class.java))
    }

    /**
     * 使用Banner
     */
    fun onUseBanner(view: View) {
        startActivity(Intent(this,BannerActivity::class.java))
    }
    /**
     * 使用加密解密
     */
    fun onEncodeOrDecode(view: View) {
        startActivity(Intent(this,EncodeOrDecodeActivity::class.java))
    }


}
