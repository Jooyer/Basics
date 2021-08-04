package cn.lvsong.lib.demo

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.lvsong.lib.library.adapter.CommonAdapter
import cn.lvsong.lib.library.adapter.MultiItemTypeAdapter
import cn.lvsong.lib.library.adapter.ViewHolder
import cn.lvsong.lib.library.listener.OnClickFastListener
import cn.lvsong.lib.library.decorator.LinearDividerItemDecoration
import cn.lvsong.lib.library.topmenu.MenuItem
import cn.lvsong.lib.library.topmenu.TopMenu
import cn.lvsong.lib.library.utils.DensityUtil
import cn.lvsong.lib.library.utils.SelectorFactory
import cn.lvsong.lib.library.utils.StatusBarUtil
import cn.lvsong.lib.library.view.MediumTextView
import cn.lvsong.lib.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_status.*

/**
 * https://segmentfault.com/q/1010000013729036
 * 当没有具体 Presenter 时,可以参考上面,写成: StatusActivity : BaseActivity<BasePresenter<*,*>>
 */
class StatusActivity : BaseActivity() {

    private lateinit var mTopMenu: TopMenu

    override fun getLayoutId() = R.layout.activity_status


    override fun useStatusManager() = true

    override fun getTransY(): Int {
        return DensityUtil.dp2pxRtInt(48)
    }

//    override fun getLoadingViewBackgroundColor() = R.color.color_2878FF

    override fun setLogic() {
        // 设置状态栏背景色透明,并在顶部添加一个padding,高度为statusBarHeight
        StatusBarUtil.transparentStatusBar(this, android.R.color.transparent, true)
        // 改变状态栏文字颜色
        StatusBarUtil.changeStatusTextColor(this,false)

        initTopMenu()
        // 如果不调用 显示内容/显示错误等,一直会显示 loading
//        mStatusManager?.showContent() // 默认1200毫秒延时,其实一般网络请求成功差不多
        mStatusManager?.delayShowContent(2000)

        acb_tips1.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_tips2.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()

        acb_tips3.background = SelectorFactory.newShapeSelector()
            .setDefaultBgColor(ContextCompat.getColor(this, R.color.color_666666))
            .setPressedBgColor(ContextCompat.getColor(this, R.color.color_333333))
            .setCornerRadius(DensityUtil.dp2pxRtInt(5))
            .create()
    }

    // 根据需要重写
    override fun getStatusBarColor() = R.color.main_theme_color

    override fun bindEvent() {
        /**
         * 注意,如果希望Toolbar不给遮挡,有以下2种解决办法
         * 1. 使用本库自带的 CustomToolbar,
         * 2. 自定义的Toolbar实现 StatusProvider 接口
         * PS: 无论上面哪种方式,都将导致控件被添加到了 RootStatusLayout 中,
         * 此时在UI界面是无法正常使用上面定义的控件, 只需使用 mStatusManager.getCustomView()
         * 即可获取 CustomToolbar 或者 自定义的Toolbar (需自行强转)
         */
        toolbar.setMoreViewListener(object :
            OnClickFastListener() {
            override fun onFastClick(v: View) {
                mTopMenu.show(v, null, null)
            }
        })
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


    private fun initTopMenu() {
        val data = arrayListOf<MenuItem>(
            MenuItem(R.drawable.ic_baseline_alarm_add_24, "展示加载"),
            MenuItem(R.drawable.ic_baseline_assignment_returned_24, "展示空视图"),
            MenuItem(R.drawable.ic_baseline_alarm_add_24, "展示错误"),
            MenuItem(R.drawable.ic_baseline_assignment_returned_24, "展示网络异常"),
            MenuItem(R.drawable.ic_baseline_alarm_add_24, "展示内容")
        )
        val adapter =
            object : CommonAdapter<MenuItem>(
                mStatusManager!!.getRootLayout().context,
                R.layout.item_top_menu,
                data
            ) {
                override fun convert(holder: ViewHolder, bean: MenuItem, position: Int) {
                    holder.getView<AppCompatImageView>(R.id.aiv_icon_left_image).visibility =
                        View.GONE
                    holder.getView<MediumTextView>(R.id.tv_text_right_text).text =
                        data[position].text
                }
            }

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                mTopMenu.dismiss()
                when (position) {
                    0 -> {
                        mStatusManager?.showLoading()
                        mStatusManager?.delayShowContent(3000)
                    }
                    1 -> mStatusManager?.showEmptyData()
                    2 -> mStatusManager?.showError()
                    3 -> mStatusManager?.showNetWorkError()
                    else -> mStatusManager?.showContent()
                }
            }
        })

        val itemDecoration =
            LinearDividerItemDecoration(
                mStatusManager!!.getRootLayout().context,
                DensityUtil.dp2pxRtInt(1F),
                ContextCompat.getColor(
                    mStatusManager!!.getRootLayout().context,
                    R.color.color_EEEEEE
                )
            )
        itemDecoration.setDividerPaddingLeft(DensityUtil.dp2pxRtInt(14F))
        itemDecoration.setDividerPaddingRight(DensityUtil.dp2pxRtInt(14F))
        mTopMenu = TopMenu(mStatusManager!!.getRootLayout().context, adapter)
            .setWidth(DensityUtil.dp2pxRtInt(150F))
            .setHeight(DensityUtil.dp2pxRtInt(200F))
            .setBackDark(true)
            .setBubbleColor(Color.DKGRAY)
            .setPaddingTop(DensityUtil.dp2pxRtInt(15))
            // 使得弹框右侧距离屏幕间隔, 如果间隔够了,箭头位置还没有对准控件中间,
            // 可以在BubbleRecyclerView所在布局中使用 brv_arrow_offset
            .setPopupXOffset(-DensityUtil.dp2pxRtInt(2F))
            // 使得弹框上下偏移
            .setPopupYOffset(-DensityUtil.dp2pxRtInt(5F))
            .setItemDecoration(itemDecoration)
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
