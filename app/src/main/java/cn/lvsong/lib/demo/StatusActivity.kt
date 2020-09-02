package cn.lvsong.lib.demo

import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.SelectorFactory
import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_status.*

/**
 * https://segmentfault.com/q/1010000013729036
 * 当没有具体 Presenter 时,可以参考上面,写成: StatusActivity : BaseActivity<BasePresenter<*,*>>
 */
class StatusActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_status

    /**
     *  可以自行测试  1/0 效果
     */
    override fun needUseImmersive() = 1

    override fun useStatusManager() = true


//    override fun setLoadingViewBackgroundColor() = R.color.color_2878FF

    override fun setLogic() {
        // 如果不调用 显示内容/显示错误等,一直会显示 loading
//        mStatusManager?.showContent() // 默认1200毫秒延时,其实一般网络请求成功差不多
        mStatusManager?.delayShowContent(3000)

        acb_loading.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_content.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_error.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_net_error.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_empty.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_tips.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()
    }

    // 根据需要重写
    override fun getStatusBarColor() = R.color.main_theme_color

    // 根据需要重写
    override fun getTransY() = DensityUtil.dp2pxRtInt(50) // 本例 toolbar高度 50dp

    override fun bindEvent() {

        // 显示Loading
        acb_loading.setOnClickListener {
            mStatusManager?.showLoading()
            mStatusManager?.delayShowContent(3000)
        }

        // 显示内容
        acb_content.setOnClickListener {
            mStatusManager?.showContent()
            Toast.makeText(this@StatusActivity,"调用showContent()显示内容",Toast.LENGTH_LONG).show()
        }

        // 显示错误
        acb_error.setOnClickListener {
            mStatusManager?.showError()
        }

        // 显示网络错误
        acb_net_error.setOnClickListener {
            mStatusManager?.showNetWorkError()
        }


        // 显示空数据
        acb_empty.setOnClickListener {
            mStatusManager?.showEmptyData()
        }
    }

    override fun getLoadingViewLayoutId(): Int {
        return R.layout.status_loading_page
    }

    override fun getEmptyDataViewLayoutId(): Int {
        return R.layout.status_empty_page
    }

    override fun getNetWorkErrorViewLayoutId(): Int {
        return R.layout.status_neterror_page
    }

    override fun getErrorViewLayoutId(): Int {
        return R.layout.status_error_page
    }


    /**
     * 点击重试则会走这里
     * 注意:  如果布局给了重试控件,则ID必须为:  view_retry_load_data
     * 注意:  如果布局给了重试控件,则ID必须为:  view_retry_load_data
     * 注意:  如果布局给了重试控件,则ID必须为:  view_retry_load_data
     */
    override fun onRetry() {
        mStatusManager?.showContent()
    }

}
