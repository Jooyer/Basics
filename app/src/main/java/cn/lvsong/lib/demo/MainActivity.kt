package cn.lvsong.lib.demo

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import cn.lvsong.lib.demo.databinding.ActivityMainBinding
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.SelectorFactory
import cn.lvsong.lib.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    /**
     *  可以自行测试  1/0 效果
     */
    override fun needUseImmersive() = 1

    override fun getStatusBarColor() = R.color.main_theme_color

    override fun getViewBinging(view: View): ActivityMainBinding {
        return ActivityMainBinding.bind(view)
    }

    override fun setLogic() {

    }

    override fun bindEvent() {
        mBinding?.btn1?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn2?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn3?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn4?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn5?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn6?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn7?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn8?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn9?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn10?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn11?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn12?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        mBinding?.btn13?.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()
    }

    /**
     * 普通刷新加载
     */
    fun onNormalRefresh(view: View) {
        startActivity(Intent(this, UnNestedRefreshActivity::class.java))
    }

    /**
     * NestedScroll嵌套滑动刷新加载
     */
    fun onNestedScrollRefresh(view: View) {
        startActivity(Intent(this, NestedScrollRefreshActivity::class.java))
    }

    /**
     * 懒加载
     */
    fun onLazyLoad(view: View) {
        startActivity(Intent(this, LazyLoadActivity::class.java))
    }

    /**
     * 使用StatusManager
     */
    fun onUserStatusManager(view: View) {
        startActivity(Intent(this, StatusActivity::class.java))
    }

    /**
     * 使用Banner
     */
    fun onUseBanner(view: View) {
        startActivity(Intent(this, BannerActivity::class.java))
    }

    /**
     * 使用加密解密
     */
    fun onEncodeOrDecode(view: View) {
        startActivity(Intent(this, EncodeOrDecodeActivity::class.java))
    }

    /**
     * 自定义 CustomToolbar
     */
    fun onCustomToolbar(view: View) {
        startActivity(Intent(this, CustomToolbarActivity::class.java))
    }

    /**
     * 自定义Menu
     */
    fun onCustomMenu(view: View) {
        startActivity(Intent(this, CustomMenuActivity::class.java))
    }

    /**
     * 聊天气泡
     */
    fun onChatBubble(view: View) {
        startActivity(Intent(this, ChatBubbleActivity::class.java))
    }

    /**
     * 自定义控件集合1
     */
    fun onCustomView1(view: View) {
        startActivity(Intent(this, CustomActivity1::class.java))
    }

    /**
     * 自定义控件集合2
     */
    fun onCustomView2(view: View) {
        startActivity(Intent(this, CustomActivity2::class.java))
    }

    fun onCustomView3(view: View) {
        startActivity(Intent(this, CustomActivity3::class.java))
    }

    fun onCustomView4(view: View) {
        startActivity(Intent(this, CustomActivity4::class.java))
    }


}
